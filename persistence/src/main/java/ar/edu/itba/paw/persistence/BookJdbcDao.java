package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.*;

@Repository
public class BookJdbcDao implements BookDao {

    final static RowMapper<Book> ROW_MAPPER = (rs, rowNum) -> new Book(
        rs.getLong("book_id"),
        rs.getString("title"),
        rs.getString("description"),
        BookGenre.valueOf(rs.getString("genre")),
        rs.getDouble("price"),
        rs.getInt("page_count"),
        rs.getInt("suggested_age"),
        rs.getDate("published_date"),
        rs.getLong("preview_id"),
        rs.getLong("cover_id"),
        UserJdbcDao.USER_ROW_MAPPER.mapRow(rs, rowNum)
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public BookJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .usingGeneratedKeyColumns("book_id")
                .withTableName("books");
    }

    @Override
    public Optional<Book> findById(long id){
        final List<Book> list = jdbcTemplate.query(
                """
                    SELECT *
                    FROM books b JOIN users u on b.writer_id = u.user_id
                    WHERE book_id = ?
                """,
                ROW_MAPPER,
                id
        );
        return list.stream().findFirst();
    }

    @Override
    public long create(String title, String description, BookGenre genre, double price, int pageCount, int suggestedAge, Date publishDate, long writerId, long previewId, long coverId) {
        Map<String, Object> bookData = new HashMap<>();

        bookData.put("title",title);
        bookData.put("description",description);
        bookData.put("genre", genre.toString());
        bookData.put("page_count", pageCount);
        bookData.put("price", price);
        bookData.put("suggested_age", suggestedAge);
        bookData.put("published_date", publishDate);
        bookData.put("writer_id", writerId);
        bookData.put("preview_id", previewId);
        bookData.put("cover_id", coverId);

        return simpleJdbcInsert.executeAndReturnKey(bookData).longValue();
    }



    @Override
    public List<Book> getAll(int offset, int limit){
        return jdbcTemplate.query(
            """
                    SELECT *
                    FROM books b JOIN users u ON b.writer_id = u.user_id
                    ORDER BY b.book_id desc
                    LIMIT ? OFFSET ?
                """,
                ROW_MAPPER,
                limit,
                offset
        );
    }

    @Override
    public int getAllSize() {
        return DaoUtils.getRowCount(jdbcTemplate, "books");
    }

    @Override
    public List<Book> searchWithParams(
            String title,
            BookGenre genre,
            Double minPrice,
            Double maxPrice,
            Integer minPageCount,
            Integer maxPageCount,
            Integer minSuggestedAge,
            Integer maxSuggestedAge,
            BookSearchOrderBy orderBy,
            int offset,
            int limit
    ){
        StringBuilder query = new StringBuilder("""
                SELECT *
                FROM books b JOIN users u on b.writer_id = u.user_id
                """);
        List<Object> params = new ArrayList<>();

        getBookSearchQueryConditions(query, params, title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);

        if(orderBy != null) {
            query.append(" ORDER BY ").append(orderBy.getColumnName());
        }

        query.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        return jdbcTemplate.query(query.toString(), ROW_MAPPER, params.toArray());
    }

    @Override
    public int getSearchSize(String title, BookGenre genre, Double minPrice, Double maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy) {
        StringBuilder conditions = new StringBuilder();
        List<Object> params = new ArrayList<>();
        getBookSearchQueryConditions(conditions, params, title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
        return DaoUtils.getRowCount(jdbcTemplate, "books", conditions.toString(), params.toArray());
    }

    private void getBookSearchQueryConditions(
            StringBuilder query,
            List<Object> params,
            String title,
            BookGenre genre,
            Double minPrice,
            Double maxPrice,
            Integer minPageCount,
            Integer maxPageCount,
            Integer minSuggestedAge,
            Integer maxSuggestedAge
    ){
            query.append("WHERE lower(title) LIKE lower(?) ");
            params.add("%" + (title!=null?title:"") + "%");
            if (genre!=null) {
                DaoUtils.addQueryCondition(query, params, " AND genre = ? ", genre.toString());
            }
            DaoUtils.addQueryCondition(query, params, " AND price >= ? ", minPrice);
            DaoUtils.addQueryCondition(query, params, " AND price <= ? ", maxPrice);
            DaoUtils.addQueryCondition(query, params, " AND page_count >= ? ", minPageCount);
            DaoUtils.addQueryCondition(query, params, " AND page_count <= ? ", maxPageCount);
            DaoUtils.addQueryCondition(query, params, " AND suggested_age >= ? ", minSuggestedAge);
            DaoUtils.addQueryCondition(query, params, " AND suggested_age <= ? ", maxSuggestedAge);
    }
}

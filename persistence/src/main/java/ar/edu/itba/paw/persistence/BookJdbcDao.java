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
import java.math.BigDecimal;
import java.util.*;

@Repository
public class BookJdbcDao implements BookDao {

    final static RowMapper<Book> ROW_MAPPER = (rs, rowNum) -> new Book(
        rs.getLong("book_id"),
        rs.getString("title"),
        rs.getString("description"),
        BookGenre.valueOf(rs.getString("genre")),
        rs.getBigDecimal("price"),
        rs.getInt("page_count"),
        rs.getInt("suggested_age"),
        rs.getDate("published_date").toLocalDate(),
        UserJdbcDao.USER_ROW_MAPPER.mapRow(rs, rowNum)
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public BookJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("books")
                .usingGeneratedKeyColumns("book_id")
                .usingColumns("title", "description", "genre", "page_count", "price", "suggested_age", "writer_id");
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
    public long create(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, long writerId) {
        Map<String, Object> bookData = new HashMap<>();

        bookData.put("title",title);
        bookData.put("description",description);
        bookData.put("genre", genre.toString());
        bookData.put("page_count", pageCount);
        bookData.put("price", price);
        bookData.put("suggested_age", suggestedAge);
        bookData.put("writer_id", writerId);

        return simpleJdbcInsert.executeAndReturnKey(bookData).longValue();
    }

    @Override
    public void modify(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge) {
        jdbcTemplate.update(
                """
                    UPDATE books
                    SET title = ?, description = ?, genre = ?, price = ?, page_count = ?, suggested_age = ?
                    WHERE book_id = ?
                """,
                title,
                description,
                genre.toString(),
                price,
                pageCount,
                suggestedAge,
                bookId
        );
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
    public long getAllSize() {
        return DaoUtils.getRowCount(jdbcTemplate, "books");
    }

    @Override
    public List<Book> searchWithParams(
            String title,
            BookGenre genre,
            BigDecimal minPrice,
            BigDecimal maxPrice,
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

        addQueryOrderByAndLimit(query, params, orderBy, offset, limit);

        return jdbcTemplate.query(query.toString(), ROW_MAPPER, params.toArray());
    }

    @Override
    public long getSearchSize(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy) {
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
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minPageCount,
            Integer maxPageCount,
            Integer minSuggestedAge,
            Integer maxSuggestedAge
    ){
            addTitleQueryCondition(query, params,title);
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

    @Override
    public List<Book> getRecommendations(Book book, int max) {
        return jdbcTemplate.query(
        """
                SELECT b.*, u.*
                FROM books b JOIN users u on b.writer_id = u.user_id LEFT JOIN orders o ON b.book_id = o.book_id AND o.status = 'COMPLETED'
                WHERE b.book_id <> ? AND (b.genre = ? OR b.writer_id = ?)
                GROUP BY b.book_id, u.user_id
                ORDER BY COUNT(o.book_id) DESC
                LIMIT ?
            """,
            ROW_MAPPER,
            book.getBookId(),
            book.getGenre().toString(),
            book.getWriter().getUserId(),
            max
        );
    }

    @Override
    public List<Book> getWriterBooks(long writerId, String title, BookSearchOrderBy orderBy,int offset, int limit) {
        StringBuilder query = new StringBuilder("""
             SELECT *
             FROM books b JOIN users u ON b.writer_id = u.user_id
        """);
        List<Object> params = new ArrayList<>();

        addTitleQueryCondition(query, params,title);

        DaoUtils.addQueryCondition(query, params, " AND writer_id = ?", writerId);

        addQueryOrderByAndLimit(query, params, orderBy, offset, limit);

        return jdbcTemplate.query(query.toString(), ROW_MAPPER, params.toArray());
    }


    @Override
    public long getWriterBooksSize(long writerId, String title) {
        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();

        addTitleQueryCondition(query, params,title);
        DaoUtils.addQueryCondition(query, params, " AND writer_id = ?", writerId);
        return DaoUtils.getRowCount(jdbcTemplate, "books", query.toString(), params.toArray());

    }

    @Override
    public List<Book> getOwnedBooks(long readerId, String title, BookSearchOrderBy orderBy, int offset, int limit) {
        StringBuilder query = new StringBuilder("""
            SELECT b.*, u.*
            FROM books b
            JOIN users u ON b.writer_id = u.user_id
            JOIN orders o ON b.book_id = o.book_id
        """);
        List<Object> params = new ArrayList<>();
        addTitleQueryCondition(query, params,title);
        DaoUtils.addQueryCondition(query, params, " AND o.buyer_id = ? ", readerId);
        query.append("AND o.status = 'COMPLETED'");
        addQueryOrderByAndLimit(query, params, orderBy, offset, limit);
        return jdbcTemplate.query(query.toString(), ROW_MAPPER, params.toArray());
    }

    @Override
    public long getOwnedBooksSize(long readerId, String title) {
        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();

        addTitleQueryCondition(query, params, title);
        DaoUtils.addQueryCondition(query, params, " AND o.buyer_id = ?", readerId);
        query.append(" AND o.status = 'COMPLETED'");

        return DaoUtils.getRowCount(jdbcTemplate, "books b JOIN orders o ON b.book_id = o.book_id", query.toString(), params.toArray());
    }

    private void addTitleQueryCondition(StringBuilder query, List<Object> params, String title) {
        query.append("WHERE lower(title) LIKE lower(?) ");
        params.add("%" + (title!=null?DaoUtils.escapeSearchString(title):"") + "%");
    }

    private void addQueryOrderByAndLimit(StringBuilder query, List<Object> params, BookSearchOrderBy orderBy,int offset, int limit) {
        if(orderBy != null) {
            query.append(" ORDER BY ").append(orderBy.getColumnName());
        }

        query.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);
    }
}


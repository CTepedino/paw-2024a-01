package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.BookDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;
import ar.edu.itba.paw.models.PublicUserInformation;
import ar.edu.itba.paw.models.BookSearchOrderBy;
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

    private final static RowMapper<Book> ROW_MAPPER = (rs, rowNum) -> new Book(
            rs.getLong("book_id"),
            rs.getString("title"),
            rs.getString("description"),
            BookGenre.valueOf(rs.getString("genre")),
            rs.getDouble("price"),
            rs.getInt("page_count"),
            rs.getLong("pdf_id"),
            rs.getLong("image_id"),
            rs.getInt("suggested_age"),
            rs.getDate("published_date"),
            new PublicUserInformation(
                    rs.getLong("writer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email")
            )
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
                    SELECT b.*, u.first_name, u.last_name, u.email
                    FROM books b JOIN users u on b.writer_id = u.user_id
                    WHERE book_id = ?
                """,
                ROW_MAPPER,
                id
        );
        return list.stream().findFirst();
    }

    @Override
    public void create(
            String title,
            String description,
            BookGenre genre,
            double price,
            int pageCount,
            long pdfId,
            long imageId,
            int suggestedAge,
            Date publishDate,
            long writerId
    ) {
        Map<String, Object> bookData = new HashMap<>();

        bookData.put("title",title);
        bookData.put("description",description);
        bookData.put("genre", genre.toString());
        bookData.put("pdf_id", pdfId);
        bookData.put("page_count", pageCount);
        bookData.put("price", price);
        bookData.put("image_id", imageId);
        bookData.put("suggested_age", suggestedAge);
        bookData.put("published_date", publishDate);
        bookData.put("writer_id", writerId);

        simpleJdbcInsert.execute(bookData);
    }


    @Override
    public List<Book> getAll(int offset, int limit){
        return jdbcTemplate.query(
            """
                    SELECT b.*, u.first_name, u.last_name, u.email
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
    public List<Book> searchByTitle(String title){
        return jdbcTemplate.query(
                """
                    SELECT * FROM books
                    WHERE title LIKE ?""",
                ROW_MAPPER,
                "%"+title+"%"
        );
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
            boolean asc
    ){
        StringBuilder sqlQuery = new StringBuilder("""
                SELECT b.*, u.first_name, u.last_name, u.email
                FROM books b JOIN users u on b.writer_id = u.user_id
                WHERE lower(title) LIKE lower(?)
                """);
        List<Object> params = new ArrayList<>();
        params.add("%" + (title!=null?title:"") + "%");
        if (genre != null){
            sqlQuery.append(" AND genre = ? ");
            params.add(genre.toString());
        }
        if (minPrice != null){
            sqlQuery.append(" AND price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null){
            sqlQuery.append(" AND price <= ? ");
            params.add(maxPrice);
        }
        if (minPageCount != null){
            sqlQuery.append(" AND page_count >= ? ");
            params.add(minPageCount);
        }
        if (maxPageCount != null){
            sqlQuery.append(" AND page_count <= ? ");
            params.add(maxPageCount);
        }

        if (minSuggestedAge != null){
            sqlQuery.append(" AND suggested_age >= ? ");
            params.add(minSuggestedAge);
        }

        if (maxSuggestedAge != null){
            sqlQuery.append(" AND suggested_age <= ? ");
            params.add(maxSuggestedAge);
        }

        if (orderBy != null){
            sqlQuery.append(" ORDER BY ?");
            params.add(orderBy + (asc? "asc":"desc"));
        }

        return jdbcTemplate.query(sqlQuery.toString(), ROW_MAPPER, params.toArray());
    }



}

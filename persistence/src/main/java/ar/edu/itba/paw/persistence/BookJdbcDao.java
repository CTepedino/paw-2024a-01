package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.BookDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;
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
            rs.getLong("writer_id"),

            rs.getString("writer_name"),
            rs.getString("writer_last_name"),
            rs.getString("writer_email")
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
                "SELECT * FROM books WHERE book_id = ?",
                ROW_MAPPER,
                id
        );
        return list.stream().findFirst();
    }

    @Override
    public Book create(
            String title,
            String description,
            BookGenre genre,
            double price,
            int pageCount,
            long pdfId,
            long imageId,
            int suggestedAge,
            Date publishDate,
            long writerId,

            String writerName,
            String writerLastName,
            String writerEmail
    ) {
        Map<String, Object> bookData = new HashMap<>();

        bookData.put("title",title);
        bookData.put("description",description);
        bookData.put("genre", genre.toString());
        bookData.put("pdf_id",pdfId);
        bookData.put("page_count",pageCount);
        bookData.put("price",price);
        bookData.put("image_id", imageId);
        bookData.put("suggested_age",suggestedAge);
        bookData.put("published_date",publishDate);
        bookData.put("writer_id",writerId);

        bookData.put("writer_name", writerName);
        bookData.put("writer_last_name", writerLastName);
        bookData.put("writer_email", writerEmail);

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(bookData);

        return new Book(
                generatedId.longValue(),
                title,
                description,
                genre,
                price,
                pageCount,
                pdfId,
                imageId,
                suggestedAge,
                publishDate,
                writerId,

                writerName,
                writerLastName,
                writerEmail
            );
    }

    @Override
    public List<Book> getAll(){
        return jdbcTemplate.query("SELECT * FROM books", ROW_MAPPER);
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
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM books WHERE lower(title) LIKE lower(?) ");
        List<Object> params = new ArrayList<>();
        params.add("%" + (title!=null?title:"") + "%");
        if (genre != null){
            sqlQuery.append("AND genre = ? ");
            params.add(genre.toString());
        }
        if (minPrice != null){
            sqlQuery.append("AND price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null){
            sqlQuery.append("AND price <= ? ");
            params.add(maxPrice);
        }
        if (minPageCount != null){
            sqlQuery.append("AND page_count >= ? ");
            params.add(minPageCount);
        }
        if (maxPageCount != null){
            sqlQuery.append("AND page_count <= ? ");
            params.add(maxPageCount);
        }

        if (minSuggestedAge != null){
            sqlQuery.append("AND suggested_age >= ? ");
            params.add(minSuggestedAge);
        }

        if (maxSuggestedAge != null){
            sqlQuery.append("AND suggested_age <= ? ");
            params.add(maxSuggestedAge);
        }

        if (orderBy != null){
            sqlQuery.append("ORDER BY ?");
            params.add(orderBy + (asc? "asc":"desc"));
        }

        return jdbcTemplate.query(sqlQuery.toString(), ROW_MAPPER, params.toArray());
    }

}

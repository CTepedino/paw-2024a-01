package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.BookDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;
import ar.edu.itba.paw.models.BookWriterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            new BookWriterInfo(
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
                new Object[] {id},
                ROW_MAPPER
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
    public List<Book> getAll(){
        return jdbcTemplate.query(
        """
                SELECT b.*, u.first_name, u.last_name, u.email
                FROM books b JOIN users u ON b.writer_id = u.user_id
            """,
            ROW_MAPPER
        );
    }

}

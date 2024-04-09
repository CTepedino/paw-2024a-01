package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.BookDao;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookJdbcDao implements BookDao {

    private final static RowMapper<Book> ROW_MAPPER = (rs, rowNum) -> new Book(rs.getLong("book_id"), rs.getString("title"), rs.getString("description"), rs.getString("genre"),rs.getDouble("price"),rs.getInt("page_numbers"),rs.getString("preview"),rs.getLong("image_id"),rs.getInt("suggested_age"),rs.getString("published_date"),rs.getLong("writer_id"));

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
        final List<Book> list = jdbcTemplate.query("SELECT * FROM books WHERE book_id = ?", new Object[] {id} ,ROW_MAPPER);
        return list.stream().findFirst();
    }

    @Override
    public List<Book> getBooks(){
        final List<Book> list = jdbcTemplate.query("SELECT * FROM books",ROW_MAPPER);
        return list;
    }

    @Override
    public Book create(String title, String description, String genre, Double price, int pageNumbers, String prev, long image_id, int suggestedAge, String published_date, long writer_id) {
        Map<String, Object> bookdata = new HashMap<>();
        bookdata.put("title",title);
        bookdata.put("description",description);
        bookdata.put("genre", genre);
        bookdata.put("preview",prev);
        bookdata.put("page_numbers",pageNumbers);
        bookdata.put("price",price);
        bookdata.put("image_id", image_id);
        bookdata.put("suggested_age",suggestedAge);
        bookdata.put("published_date",published_date);
        bookdata.put("writer_id",writer_id);
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(bookdata);
        return new Book(generatedId.intValue(),title,description,genre,price,pageNumbers,prev,image_id,suggestedAge,published_date,writer_id);
    }


}

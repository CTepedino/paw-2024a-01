package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.BookDao;
import ar.edu.itba.paw.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BookJdbcDao implements BookDao {
    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public BookJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .usingGeneratedKeyColumns("book_id", "title" /*, "description","genra","prev","page_numbers","price","image","suggested_age", "published_date","writer_email"*/)
                .withTableName("book");
    }
/*
    @Override
    public Book create(String title, String description, String genra, double price, int pageNumbers, String prev, String image, int suggestedAge, String publishedDate, String writerEmail) {
        Map<String, Object> bookdata = new HashMap<>();
        bookdata.put("title",title);
        bookdata.put("description",description);
        bookdata.put("genra", genra);
        bookdata.put("prev",prev);
        bookdata.put("page_numbers",pageNumbers);
        bookdata.put("price",price);
        bookdata.put("image", image);
        bookdata.put("suggested_age",suggestedAge);
        bookdata.put("published_date",publishedDate);
        bookdata.put("writer_email",writerEmail);
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(bookdata);
        return new Book(generatedId.intValue(),title/*,description,genra,price,pageNumbers,prev,image,suggestedAge,publishedDate,writerEmail);
    }
*/
    @Override
    public Book create(String title) {
        Map<String, Object> bookdata = new HashMap<>();
        bookdata.put("title",title);
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(bookdata);
        return new Book(generatedId.intValue(),title);
    }
}

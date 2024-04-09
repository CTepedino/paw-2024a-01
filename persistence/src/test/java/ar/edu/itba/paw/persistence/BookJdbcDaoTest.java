package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;
import ar.edu.itba.paw.models.Writer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.List;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class BookJdbcDaoTest {

    private static final String TITLE = "myBook";
    private static final String DESCRIPTION = "a book";
    private static final BookGenre GENRE = BookGenre.FICTION;
    private static final double PRICE = 120.25;
    private static final int PAGE_COUNT = 120;
    private static final int SUGGESTED_AGE = 3;
    private static final long IMAGE_ID = 1;
    private static final long PDF_ID = 1;
    private static final long WRITER_ID = 1;
    private static final Date DATE = new Date(System.currentTimeMillis());

    @Autowired
    private DataSource ds;

    @Autowired
    private BookJdbcDao bookDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "books");
    }

    @Test
    public void testCreate(){
        Book book = bookDao.create(
                TITLE,
                DESCRIPTION,
                GENRE,
                PRICE,
                PAGE_COUNT,
                PDF_ID,
                IMAGE_ID,
                SUGGESTED_AGE,
                DATE,
                WRITER_ID
        );

        Assert.assertNotNull(book);
        Assert.assertEquals(TITLE, book.getTitle());
        Assert.assertEquals(PRICE, book.getPrice(), 0);
        Assert.assertEquals(DATE, book.getPublishDate());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "books", "book_id = " + book.getBookId() ));
    }

    @Test
    public void testGetAll(){
        List<Book> list = bookDao.getAll();

        Assert.assertNotNull(list);
        Assert.assertEquals(list.size(), JdbcTestUtils.countRowsInTable(jdbcTemplate, "books"));
    }

}

package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class BookJdbcDaoTest {

    private static final long NON_EXISTING_ID = 99999;
    private static final long EXISTING_ID = 1;

    private static final long EXISTING_WRITER_ID = 2;
    private static final long NON_EXISTING_WRITER_ID = 9999;
    
    @Autowired
    private DataSource ds;

    @Autowired
    private BookJdbcDao bookDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testFindByIdExisting(){
        Optional<Book> maybeBook = bookDao.findById(EXISTING_ID);

        assertNotNull(maybeBook);
        assertTrue(maybeBook.isPresent());
    }

    @Test
    public void testFindByIdNonExisting(){
        Optional<Book> maybeBook = bookDao.findById(NON_EXISTING_ID);

        assertNotNull(maybeBook);
        assertTrue(maybeBook.isEmpty());
    }

    @Test
    public void testCreateOK(){
        long bookId = bookDao.create(
                "",
                "",
                BookGenre.FICTION,
                new BigDecimal(105),
                123,
                3,
                EXISTING_WRITER_ID
        );

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere( jdbcTemplate, "books", "book_id = " + bookId));
    }

    @Test
    public void testModify(){
        bookDao.modify(1, "MODIFIED BOOK MODIFIED BOOK MODIFIED BOOK", "", BookGenre.BIOGRAPHY, new BigDecimal(1), 1, 1, false );

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "books", "title  = 'MODIFIED BOOK MODIFIED BOOK MODIFIED BOOK'"));
    }

    @Test
    public void testCreateNonExistentWriter(){
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "books");

        assertThrows(
                DataIntegrityViolationException.class,
                () -> bookDao.create(
                        "",
                        "",
                        BookGenre.FICTION,
                        new BigDecimal(105),
                        123,
                        3,
                        NON_EXISTING_WRITER_ID
                )
        );
        assertEquals(rows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "books"));
    }


    @Test
    public void testGetAllLimitExceeds(){
        List<Book> books = bookDao.getAll(0,99999);

        assertNotNull(books);
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "books", "is_paused = FALSE"), books.size());
    }

    @Test
    public void testGetAllLimitNotExceeds(){
        List<Book> books = bookDao.getAll(0,1);

        assertNotNull(books);
        assertEquals(1, books.size());
    }

    @Test
    public void testGetAllWithOffset(){
        List<Book> books = bookDao.getAll(9999,1);

        assertNotNull(books);
        assertEquals(0, books.size());
    }

    @Test
    public void testGetAllSize(){
        long size = bookDao.getAllSize();

        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "books", "is_paused = FALSE"), size);
    }


    @Test
    public void testSearchNullParams(){
        List<Book> books = bookDao.searchWithParams(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                99999
        );

        assertNotNull(books);
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "books", "is_paused = FALSE"), books.size());
    }

    @Test
    public void testSearchWithParams(){
        List<Book> books = bookDao.searchWithParams(
                "my book",
                BookGenre.FICTION,
                new BigDecimal(1000),
                new BigDecimal(2000),
                400,
                600,
                10,
                10,
                BookSearchOrderBy.PRICE_ASC,
                0,
                99999
        );


        assertNotNull(books);
        assertEquals(JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate,
                "books",
                """
                            title LIKE '%my book%'
                            AND genre = 'FICTION'
                            AND price >= 1000.00 AND price <= 2000.00
                            AND page_count >= 400 AND page_count <= 600
                            AND suggested_age >= 10 AND suggested_age <= 10
                            """),
                books.size());

        assertEquals(getSortedByPriceAscList(books), books);
    }

    private List<Book> getSortedByPriceAscList(List<Book> books){
        List<Book> booksCopy = new ArrayList<>(books);
        booksCopy.sort(Comparator.comparing(Book::getPrice));
        return booksCopy;
    }

    @Test
    public void testGetGenresByBookCount(){
        List<BookGenre> genres = bookDao.getGenresByBookCount(5, 0);

        assertNotNull(genres);
        assertEquals(BookGenre.FICTION, genres.getFirst());
    }

    @Test
    public void testRecheckedPaused(){
        jdbcTemplate.update("INSERT INTO book_files (id, file) VALUES (3, '')");

        boolean keepPaused = bookDao.recheckPaused(3);

        assertFalse(keepPaused);
    }

    @Test
    public void testGetWriterBooks(){
        List<Book> books = bookDao.getWriterBooks(2, "", BookSearchOrderBy.PAGE_COUNT_ASC, 0, 999);

        assertNotNull(books);
        for (Book book : books){
            assertEquals(2,book.getWriter().getUserId());
        }
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "books", "writer_id = 2"),books.size());
    }

    @Test
    public void testGetOwnedBooks(){
        List<Book> books = bookDao.getOwnedBooks(2, "", BookSearchOrderBy.PAGE_COUNT_ASC, 0, 999);

        assertNotNull(books);
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "books b JOIN orders o ON b.book_id = o.book_id", "o.buyer_id = 2 AND o.status = 'COMPLETED'"),books.size());
    }
}

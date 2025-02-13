package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.books.BookSearchQueryDTO;
import ar.edu.itba.paw.models.users.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static ar.edu.itba.paw.persistence.TestUtils.getRowCount;
import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class BookJpaDaoTest {

    private static final long EXISTING_WRITER_ID = 102;
    private static final User TEST_USER = new User(EXISTING_WRITER_ID, "","", "", "",false, Locale.US);

    private static final long NON_EXISTING_ID = 99999;
    private static final long EXISTING_ID = 101;
    private static final String TITLE = "title";
    private static final Book BOOK = new Book(EXISTING_ID, TITLE, "", BookGenre.FICTION, BigDecimal.ONE, 1, 1, LocalDate.now(), TEST_USER, false);

    private static final BookSearchQueryDTO QUERY_ANY = new BookSearchQueryDTO();


    @Autowired
    private BookJpaDao bookDao;

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp(){
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
        Book book = bookDao.create(
                TITLE,
                "",
                BookGenre.FICTION,
                new BigDecimal(105),
                123,
                3,
                LocalDate.now(),
                TEST_USER,
                false
        );

        assertEquals(1, getRowCount(em, "FROM books WHERE book_id = " + book.getBookId() + "AND title = '" + TITLE + "'"));
    }

    @Test
    public void testModify(){
        Book book = em.find(Book.class, EXISTING_ID);

        bookDao.modify(book, "MODIFIED BOOK MODIFIED BOOK MODIFIED BOOK", "", BookGenre.BIOGRAPHY, new BigDecimal(1), 1, 1 );

        assertEquals(1, getRowCount(em, "FROM books WHERE title  = 'MODIFIED BOOK MODIFIED BOOK MODIFIED BOOK'"));
    }

    @Test
    public void testGetAllLimitExceeds(){
        QUERY_ANY.setPageSize(99999);
        QUERY_ANY.setPageNumber(1);
        List<Book> books = bookDao.searchWithParams(QUERY_ANY);

        assertNotNull(books);
        assertEquals(getRowCount(em, "FROM books WHERE is_paused = FALSE"), books.size());
    }

    @Test
    public void testGetAllLimitNotExceeds(){
        QUERY_ANY.setPageSize(1);
        QUERY_ANY.setPageNumber(1);
        QUERY_ANY.setOrderBy(BookSearchOrderBy.PAGE_COUNT_ASC);
        List<Book> books = bookDao.searchWithParams(QUERY_ANY);

        assertNotNull(books);
        assertTrue(books.size() <= 1);
    }

    @Test
    public void testGetAllWithOffset(){
        QUERY_ANY.setPageSize(1);
        QUERY_ANY.setPageNumber(99999);
        List<Book> books = bookDao.searchWithParams(QUERY_ANY);

        assertNotNull(books);
        assertEquals(0, books.size());
    }

    @Test
    public void testGetAllSize(){
        long size = bookDao.getSearchSize(QUERY_ANY);

        assertEquals(getRowCount(em, "FROM books WHERE is_paused = FALSE"), size);
    }

    @Test
    public void testSearchNullParams(){
        QUERY_ANY.setOrderBy(BookSearchOrderBy.PRICE_ASC);
        QUERY_ANY.setPageNumber(1);
        QUERY_ANY.setPageSize(99999);
        List<Book> books = bookDao.searchWithParams(QUERY_ANY);

        assertNotNull(books);
        assertEquals(getRowCount(em, "FROM books WHERE is_paused = FALSE"), books.size());
    }

    @Test
    public void testSearchWithParams(){
        BookSearchQueryDTO QUERY = new BookSearchQueryDTO(
                "my book", BookGenre.FICTION,
                new BigDecimal(1000),new BigDecimal(2000),
                400, 600,
                10, 10,
                null, null, null,
                BookSearchOrderBy.PRICE_ASC,
                1, 99999
        );

        List<Book> books = bookDao.searchWithParams(QUERY);

        assertNotNull(books);
        assertEquals(getRowCount(
                        em,
                    """
                                FROM books b LEFT JOIN deals d ON b.book_id = d.id
                                WHERE b.title LIKE '%my book%'
                                AND b.genre = 'FICTION'
                                AND ((d.price IS NULL AND b.price >= 1000.00) OR d.price >= 1000.00 )
                                AND ((d.price IS NULL AND b.price <= 2000.00) OR d.price <= 2000.00 )
                                AND b.page_count >= 400 AND b.page_count <= 600
                                AND b.suggested_age >= 10 AND b.suggested_age <= 10
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
    public void testGetWriterBooks(){
        QUERY_ANY.setPageNumber(1);
        QUERY_ANY.setPageSize(999);
        QUERY_ANY.setWriterId((long)2);
        QUERY_ANY.setOrderBy(BookSearchOrderBy.PAGE_COUNT_ASC);

        List<Book> books = bookDao.searchWithParams(QUERY_ANY);

        assertNotNull(books);
        for (Book book : books){
            assertEquals(2,book.getWriter().getUserId());
        }
        assertEquals(getRowCount(em, "FROM books WHERE writer_id = 2"),books.size());
    }

    @Test
    public void testGetOwnedBooks(){
        QUERY_ANY.setPageNumber(1);
        QUERY_ANY.setPageSize(999);
        QUERY_ANY.setOwnerId((long)2);
        QUERY_ANY.setOrderBy(BookSearchOrderBy.PAGE_COUNT_ASC);
        QUERY_ANY.setRecommendedByUserOnly(false);

        List<Book> books = bookDao.searchWithParams(QUERY_ANY);

        assertNotNull(books);
        assertEquals(getRowCount(em, "FROM books b JOIN orders o ON b.book_id = o.book_id WHERE o.buyer_id = 2 AND o.status = 'COMPLETED'"),books.size());
    }

}
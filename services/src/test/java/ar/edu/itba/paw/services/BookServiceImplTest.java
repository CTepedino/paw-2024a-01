package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.books.BookSearchQueryDTO;
import ar.edu.itba.paw.models.deals.Deal;
import ar.edu.itba.paw.models.exception.InvalidPageException;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {

    private static final long SIZE = 100;
    private static final int LIMIT = 9;

    private static final long BOOK_ID = 1;

    private static final Book TEST_BOOK_1 =  new Book(1, "Book 1",
            "", BookGenre.SCIENCE_FICTION, new BigDecimal(500),
            200, 15, LocalDate.now(),  new User(BOOK_ID,"","","","", false, Locale.US), false);


    private static final Book TEST_BOOK_2 =  new Book(2, "Book 2",
            "", BookGenre.FANTASY, new BigDecimal(200),
            100, 12, LocalDate.now(),  new User(2,"","","","", false, Locale.US), false);

    private static final BookSearchQueryDTO QUERY_ANY = new BookSearchQueryDTO();


    private static final User TEST_WRITER = new User("", "", "", "", false, Locale.US);

    private static final List<UserRoles> MOCKED_ROLES = new ArrayList<>();

    @Mock
    private BookDao bookDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private UserService us;

    @InjectMocks
    private BookServiceImpl bookService;

    @Before
    public void setup(){
        TEST_BOOK_1.setDeal(new Deal(1, BigDecimal.ONE, LocalDate.now(), LocalDate.now().plusDays(7)));

        Mockito.when(us.getLoggedUser()).thenReturn(Optional.of(TEST_WRITER));

        Mockito.when(bookDao.create(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any(BigDecimal.class),
                Mockito.anyInt(),
                Mockito.anyInt(),
                Mockito.any(),
                Mockito.any(User.class),
                Mockito.anyBoolean()
        )).thenReturn(TEST_BOOK_1);
        TEST_WRITER.setRoles(MOCKED_ROLES);
    }

    @Test
    public void testPublishNotWriter(){
        MOCKED_ROLES.clear();
        MOCKED_ROLES.add(UserRoles.READER);

        long bookId = bookService.create(
                "",
                "",
                BookGenre.FICTION,
                new BigDecimal(1),
                1,
                1,
                LocalDate.now()
        );

        assertEquals(BOOK_ID, bookId);
    }

    @Test
    public void testPublishAsWriter(){
        MOCKED_ROLES.clear();
        MOCKED_ROLES.add(UserRoles.READER);
        MOCKED_ROLES.add(UserRoles.WRITER);


        long bookId = bookService.create(
                "",
                "",
                BookGenre.FICTION,
                new BigDecimal(1),
                1,
                1,
                LocalDate.now()
        );

        assertEquals(BOOK_ID, bookId);
        assertEquals(MOCKED_ROLES.indexOf(UserRoles.WRITER), MOCKED_ROLES.lastIndexOf(UserRoles.WRITER));
    }


    @Test
    public void testListBooks(){
        int page = 2;
        List<Book> mockedBooks = Collections.nCopies((int)SIZE, TEST_BOOK_1);
        QUERY_ANY.setPageNumber(page);
        QUERY_ANY.setPageSize(LIMIT);

        Mockito.when(bookDao.searchWithParams(QUERY_ANY))
                .thenReturn(mockedBooks.subList((page-1)*LIMIT, page*LIMIT));
        Mockito.when(bookDao.getSearchSize(QUERY_ANY))
                .thenReturn(SIZE);

        PaginatedContent<Book> books = bookService.listBooks(QUERY_ANY);

        assertNotNull(books);
        assertEquals(page, books.getPageNumber());
        assertEquals(mockedBooks.subList((page-1)*LIMIT, page*LIMIT), books.getPage());
        assertEquals(LIMIT, books.getPageSize());
        assertEquals((int)Math.ceil((double)SIZE/LIMIT), books.getPageCount());
        assertEquals(SIZE, books.getTotalSize());
    }

    @Test
    public void testListBooksInvalidPage(){
        QUERY_ANY.setPageNumber(0);
        assertThrows(
                InvalidPageException.class,
                ()->bookService.listBooks(QUERY_ANY)
        );
    }

    @Test
    public void testListBooksByNewDeals(){
        List<Book> mockedBooks = List.of(TEST_BOOK_1, TEST_BOOK_2);
        QUERY_ANY.setOrderBy(BookSearchOrderBy.NEW_DEALS);
        QUERY_ANY.setPageNumber(1);
        QUERY_ANY.setPageSize(10);

        Mockito.when(bookDao.getBooksWithNewDeals(QUERY_ANY))
                .thenReturn(mockedBooks.stream().filter(b -> b.getDeal() != null).toList());
        Mockito.when(bookDao.getBooksWithNewDealsSize(QUERY_ANY))
                .thenReturn((long)2);

        PaginatedContent<Book> books = bookService.listBooks(QUERY_ANY);

        assertNotNull(books);
        assertNotNull(books.getPage());
        for (Book book: books.getPage()){
            assertNotNull(book.getDeal());
        }
    }

}

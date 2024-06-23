package ar.edu.itba.paw.services;
import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.exception.InvalidPageException;
import ar.edu.itba.paw.models.users.User;
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

    private static final Book TEST_BOOK =  new Book(1, "", "", BookGenre.FICTION, new BigDecimal(1), 1, 1, LocalDate.now(),  new User(1,"","","","", false, Locale.US), false);

    private static final List<BookGenre> FULL_BOOK_GENRE_LIST = Arrays.asList(Arrays.copyOfRange(BookGenre.values(), 0, 12));
    private static final List<BookGenre> HALF_FULL_BOOK_GENRE_LIST = Arrays.asList(Arrays.copyOfRange(BookGenre.values(), 0, 6));

    @Mock
    private BookDao bookDao;

    @InjectMocks
    private BookServiceImpl bookService;


    @Test
    public void testGetAll(){
        int page = 2;
        List<Book> mockedBooks = Collections.nCopies((int)SIZE, TEST_BOOK);
        Mockito.when(bookDao.getAllSize()).thenReturn(SIZE);
        Mockito.when(bookDao.getAll(Mockito.eq((page-1)*LIMIT), Mockito.eq(LIMIT)))
                .thenReturn(mockedBooks.subList((page-1)*LIMIT, page*LIMIT));

        PaginatedContent<Book> books = bookService.getAll(page, LIMIT);

        assertNotNull(books);
        assertEquals(page, books.getPageNumber());
        assertEquals(mockedBooks.subList((page-1)*LIMIT, page*LIMIT), books.getPage());
        assertEquals(LIMIT, books.getPageSize());
        assertEquals((int)Math.ceil((double)SIZE/LIMIT), books.getPageCount());
        assertEquals(SIZE, books.getTotalSize());
    }

    @Test
    public void testGetAllInvalidPage(){
        assertThrows(
                InvalidPageException.class,
                ()->bookService.getAll(0, LIMIT)
        );
    }

    @Test
    public void testGetGenresByBookCountEmptyBookCount(){
        List<BookGenre> emptyList = new ArrayList<>();
        Mockito.when(bookDao.getGenresByBookCount(Mockito.anyInt(), Mockito.anyInt())).thenReturn(emptyList);

        List<BookGenre> genres = bookService.getGenresByBookCount();

        assertNotNull(genres);
        assertEquals(12, genres.size());
    }

    @Test
    public void testGetGenresByBookCountFullBookCount(){
        Mockito.when(bookDao.getGenresByBookCount(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new ArrayList<>(FULL_BOOK_GENRE_LIST));

        List<BookGenre> genres = bookService.getGenresByBookCount();

        assertNotNull(genres);
        assertEquals(12, genres.size());
    }

    @Test
    public void testGetGenresByBookCountHalfFullBookCount(){
        Mockito.when(bookDao.getGenresByBookCount(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new ArrayList<>(HALF_FULL_BOOK_GENRE_LIST));

        List<BookGenre> genres = bookService.getGenresByBookCount();

        assertNotNull(genres);
        assertEquals(12, genres.size());
        assertTrue(hasNoDuplicates(genres));
    }

    private boolean hasNoDuplicates(List<BookGenre> genres){
        HashSet<BookGenre> set = new HashSet<>();
        for (BookGenre genre : genres) {
            if (!set.add(genre)) {
                return false;
            }
        }
        return true;
    }

}
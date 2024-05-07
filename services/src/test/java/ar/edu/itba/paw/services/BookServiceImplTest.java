package ar.edu.itba.paw.services;
import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.files.BookPreviewDao;
import ar.edu.itba.paw.interfaces.dao.files.CoverImageDao;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.exception.InvalidPageException;
import ar.edu.itba.paw.models.users.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {

    private static final long SIZE = 100;
    private static final int LIMIT = 9;

    private static final Book TEST_BOOK =  new Book(1, "", "", BookGenre.FICTION, new BigDecimal(1), 1, 1, LocalDate.now(),  new User(1,"","","",""));

    @Mock
    private BookDao bookDao;

    @Mock
    private BookPreviewDao previewDao;

    @Mock
    private CoverImageDao coverDao;

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

}

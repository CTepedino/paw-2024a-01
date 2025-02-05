package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookGenreOrderBy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GenreServiceImplTest {

    private static final List<BookGenre> FULL_BOOK_GENRE_LIST = Arrays.asList(Arrays.copyOfRange(BookGenre.values(), 0, 12));
    private static final List<BookGenre> HALF_FULL_BOOK_GENRE_LIST = Arrays.asList(Arrays.copyOfRange(BookGenre.values(), 0, 6));

    @Mock
    private BookDao bookDao;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    public void testGetGenresByBookCountEmptyBookCount(){
        List<BookGenre> emptyList = new ArrayList<>();
        Mockito.when(bookDao.getGenresByBookCount(Mockito.anyInt(), Mockito.anyInt())).thenReturn(emptyList);

        List<BookGenre> genres = genreService.getGenres(BookGenreOrderBy.BOOK_COUNT, 1, 12).getPage();

        assertNotNull(genres);
        assertEquals(12, genres.size());
    }

    @Test
    public void testGetGenresByBookCountFullBookCount(){
        Mockito.when(bookDao.getGenresByBookCount(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new ArrayList<>(FULL_BOOK_GENRE_LIST));

        List<BookGenre> genres = genreService.getGenres(BookGenreOrderBy.BOOK_COUNT, 1, 12).getPage();

        assertNotNull(genres);
        assertEquals(12, genres.size());
    }

    @Test
    public void testGetGenresByBookCountHalfFullBookCount(){
        Mockito.when(bookDao.getGenresByBookCount(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new ArrayList<>(HALF_FULL_BOOK_GENRE_LIST));

        List<BookGenre> genres = genreService.getGenres(BookGenreOrderBy.BOOK_COUNT, 1, 12).getPage();

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

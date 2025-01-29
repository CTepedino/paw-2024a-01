package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceImplTest {

    private static final Book A_BOOK = new Book(1,"", "", BookGenre.BIOGRAPHY, BigDecimal.ONE, 1, 1, LocalDate.now(), null, false);
    private static final Book ANOTHER_BOOK = new Book(2,"", "", BookGenre.BIOGRAPHY, BigDecimal.ONE, 1, 1, LocalDate.now(), null, false);

    @Mock
    ReviewDao reviewDao;

    @Mock
    UserService userService;

    @InjectMocks
    ReviewServiceImpl reviewService;

    /*@Test
    public void testGetBookRatings(){
        Mockito.when(reviewDao.getAverageRating(1)).thenReturn(10);
        Mockito.when(reviewDao.getAverageRating(2)).thenReturn(7);

        Map<Long, Float> ratings = reviewService.getBookRatings(List.of(A_BOOK, ANOTHER_BOOK));

        Assert.assertNotNull(ratings);
        Assert.assertEquals(2, ratings.size());
        Assert.assertEquals(5, (double) ratings.get(1L), 0.1);
        Assert.assertEquals(3.5, (double) ratings.get(2L), 0.1);
    }

    @Test
    public void testGetBookRatingsEmptyList(){
        Map<Long, Float> ratings = reviewService.getBookRatings(List.of());

        Assert.assertNotNull(ratings);
        Assert.assertEquals(0, ratings.size());
    }*/

}

package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.models.books.BookAnalytics;
import ar.edu.itba.paw.models.users.UserAnalytics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AnalyticsServiceImplTest {

    private static final long ID = 1;
    private static final long ORDERS = 2;
    private static final BigDecimal SALES = BigDecimal.TEN;

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @Before
    public void setup(){
        Mockito.when(orderDao.getWriterTotalOrdersPerMonth(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(ORDERS);
        Mockito.when(orderDao.getWriterTotalSalesPerMonth(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(SALES);

        Mockito.when(orderDao.getBookTotalOrdersPerMonth(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(ORDERS);
        Mockito.when(orderDao.getBookTotalSalesPerMonth(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(SALES);
    }

    @Test
    public void testGetUserAnalytics(){
        UserAnalytics userAnalytics = analyticsService.getUserAnalytics(ID, YearMonth.now());

        assertNotNull(userAnalytics);
        assertEquals(ID, userAnalytics.getUserId());
        assertEquals(ORDERS, userAnalytics.getOrderCount());
        assertEquals(SALES, userAnalytics.getTotalSales());
    }

    @Test
    public void testGetBookAnalytics(){
        BookAnalytics bookAnalytics = analyticsService.getBookAnalytics(ID, YearMonth.now());

        assertNotNull(bookAnalytics);
        assertEquals(ID, bookAnalytics.getBookId());
        assertEquals(ORDERS, bookAnalytics.getOrderCount());
        assertEquals(SALES, bookAnalytics.getTotalSales());
    }
}

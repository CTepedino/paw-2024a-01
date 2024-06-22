package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.YearMonth;

@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceImplTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @Test
    public void testGetSalesIncrease(){
        Mockito.when(orderDao.getTotalSalesForMonth(Mockito.anyLong(), Mockito.anyInt(), Mockito.eq(YearMonth.now().getMonthValue()))).thenReturn(new BigDecimal(100));
        Mockito.when(orderDao.getTotalSalesForMonth(Mockito.anyLong(), Mockito.anyInt(), Mockito.eq(YearMonth.now().minusMonths(1).getMonthValue()))).thenReturn(new BigDecimal(50));

        String increase = analyticsService.getSalesIncrease(1);

        Assert.assertEquals("+100%", increase);
    }

    @Test
    public void testGetSalesDecrease(){
        Mockito.when(orderDao.getTotalSalesForMonth(Mockito.anyLong(), Mockito.anyInt(), Mockito.eq(YearMonth.now().getMonthValue()))).thenReturn(new BigDecimal(0));
        Mockito.when(orderDao.getTotalSalesForMonth(Mockito.anyLong(), Mockito.anyInt(), Mockito.eq(YearMonth.now().minusMonths(1).getMonthValue()))).thenReturn(new BigDecimal(100));

        String decrease = analyticsService.getSalesIncrease(1);

        Assert.assertEquals("-100%", decrease);
    }

    @Test
    public void testGetOrdersIncrease(){
        Mockito.when(orderDao.getTotalOrdersForMonthForWriter(Mockito.anyLong(), Mockito.anyInt(), Mockito.eq(YearMonth.now().getMonthValue()))).thenReturn(10L);
        Mockito.when(orderDao.getTotalOrdersForMonthForWriter(Mockito.anyLong(), Mockito.anyInt(), Mockito.eq(YearMonth.now().minusMonths(1).getMonthValue()))).thenReturn(5L);

        String increase = analyticsService.getOrdersIncrease(1);

        Assert.assertEquals("+100%", increase);
    }

    @Test
    public void testGetOrdersDecrease(){
        Mockito.when(orderDao.getTotalOrdersForMonthForWriter(Mockito.anyLong(), Mockito.anyInt(), Mockito.eq(YearMonth.now().getMonthValue()))).thenReturn(5L);
        Mockito.when(orderDao.getTotalOrdersForMonthForWriter(Mockito.anyLong(), Mockito.anyInt(), Mockito.eq(YearMonth.now().minusMonths(1).getMonthValue()))).thenReturn(10L);

        String decrease = analyticsService.getOrdersIncrease(1);

        Assert.assertEquals("-50%", decrease);
    }
}

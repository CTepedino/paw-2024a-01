package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import ar.edu.itba.paw.models.books.BookAnalytics;
import ar.edu.itba.paw.models.users.UserAnalytics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderDao orderDao;

    @Autowired
    public AnalyticsServiceImpl(final OrderDao orderDao){
        this.orderDao= orderDao;
    }

    @Transactional(readOnly = true)
    @Override
    public UserAnalytics getUserAnalytics(long userId, YearMonth yearMonth) {
        return new UserAnalytics(
                userId,
                orderDao.getWriterTotalOrdersPerMonth(userId, yearMonth.getYear(), yearMonth.getMonthValue()),
                orderDao.getWriterTotalSalesPerMonth(userId, yearMonth.getYear(), yearMonth.getMonthValue())
        );
    }

    @Transactional(readOnly = true)
    @Override
    public BookAnalytics getBookAnalytics(long bookId, YearMonth yearMonth) {
        return new BookAnalytics(
                bookId,
                orderDao.getBookTotalOrdersPerMonth(bookId, yearMonth.getYear(), yearMonth.getMonthValue()),
                orderDao.getBookTotalSalesPerMonth(bookId, yearMonth.getYear(), yearMonth.getMonthValue())
        );
    }
}

package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import ar.edu.itba.paw.models.books.BookAnalytics;
import ar.edu.itba.paw.models.users.UserAnalytics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderDao orderDao;

    private final static Logger LOGGER = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    @Autowired
    public AnalyticsServiceImpl(final OrderDao orderDao){
        this.orderDao= orderDao;
    }

    @Transactional(readOnly = true)
    @Override
    public UserAnalytics getUserAnalytics(long userId, YearMonth yearMonth) {
        LOGGER.atDebug().setMessage("Obtained monthly analytics for user {} at month {}").addArgument(userId).addArgument(yearMonth).log();

        return new UserAnalytics(
                userId,
                orderDao.getWriterTotalOrdersPerMonth(userId, yearMonth.getYear(), yearMonth.getMonthValue()),
                orderDao.getWriterTotalSalesPerMonth(userId, yearMonth.getYear(), yearMonth.getMonthValue()),
                yearMonth
        );
    }

    @Transactional(readOnly = true)
    @Override
    public BookAnalytics getBookAnalytics(long bookId, YearMonth yearMonth) {
        LOGGER.atDebug().setMessage("Obtained monthly analytics for book {} at month {}").addArgument(bookId).addArgument(yearMonth).log();

        return new BookAnalytics(
                bookId,
                orderDao.getBookTotalOrdersPerMonth(bookId, yearMonth.getYear(), yearMonth.getMonthValue()),
                orderDao.getBookTotalSalesPerMonth(bookId, yearMonth.getYear(), yearMonth.getMonthValue()),
                yearMonth
        );
    }
}

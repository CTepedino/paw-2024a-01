package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderDao orderDao;

    @Autowired
    public AnalyticsServiceImpl(final OrderDao orderDao){
        this.orderDao= orderDao;
    }

    @Transactional(readOnly = true)
    @Override
    public long getTotalOrdersForWriterForMonth(long writerId, int year, int month){
        return orderDao.getTotalOrdersForMonthForWriter(writerId, year, month);
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getTotalSalesForWriterForMonth(long writerId, int year, int month){
        //TODO: la API deberia devolver el formato o solo el número?
        return orderDao.getTotalSalesForMonth(writerId, year, month);
    }

    @Transactional(readOnly = true)
    @Override
    public long getTotalOrdersForBookForMonth(long bookId, int year, int month) {
        return orderDao.getTotalOrdersForMonthForBook(bookId, year, month);
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getTotalSalesForBookForMonth(long bookId, int year, int month) {
        return orderDao.getTotalSalesForMonthForBook(bookId, year, month);
    }


}

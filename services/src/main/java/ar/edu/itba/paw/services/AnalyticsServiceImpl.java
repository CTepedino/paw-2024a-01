package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import ar.edu.itba.paw.models.books.AnalyticsBook;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderDao orderDao;
    private final BookDao bookDao;

    @Autowired
    public AnalyticsServiceImpl(final OrderDao orderDao, BookDao bookDao){
        this.orderDao=orderDao;
        this.bookDao = bookDao;
    }

    @Transactional(readOnly = true)
    @Override
    public Long getTotalOrdersForWriter(long writerId){
        return orderDao.getTotalOrdersForWriter(writerId);
    }

    @Transactional(readOnly = true)
    @Override
    public Long getTotalOrdersForBook(long bookId){
        return orderDao.getTotalOrdersForBook(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getTotalSales(long writerId){
        return orderDao.getTotalSales(writerId);
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getTotalSalesForBook(long bookId){
        return orderDao.getTotalSalesForBook(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getTotalSalesForMonth(long writerId, int year, int month){
        return orderDao.getTotalSalesForMonth(writerId, year, month);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getTop5BooksByWriter(long writerId){
        return orderDao.getTop5BooksByWriter(writerId);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsBook> getBooksByWriterWithAnalytics(long writerId) {
        List<Book> books = bookDao.getWriterBooks(writerId, "", BookSearchOrderBy.PRICE_ASC, 0, 10);
        return books.stream()
                .map(book -> new AnalyticsBook(book, getTotalOrdersForBook(book.getBookId()), getTotalSalesForBook(book.getBookId())))
                .sorted(Comparator.comparingLong(AnalyticsBook::getTotalOrders).reversed()
                        .thenComparing(Comparator.comparing(AnalyticsBook::getTotalSales).reversed()))
                .collect(Collectors.toList());
    }

}

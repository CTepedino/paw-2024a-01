package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.AnalyticsBook;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.InvalidPageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderDao orderDao;

    @Autowired
    public AnalyticsServiceImpl(final OrderDao orderDao){
        this.orderDao= orderDao;
    }

    @Transactional(readOnly = true)
    @Override
    public long getTotalOrdersForWriter(long writerId){
        return orderDao.getWriterOrdersSize(writerId, "", null);
    }

    @Transactional(readOnly = true)
    @Override
    public long getTotalOrdersForBook(long bookId){
        return orderDao.getTotalOrdersForBook(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public long getTotalOrdersForWriterForMonth(long writerId, int year, int month){
        return orderDao.getTotalOrdersForMonthForWriter(writerId, year, month);
    }

    @Transactional(readOnly = true)
    @Override
    public String getTotalSales(long writerId){
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(LocaleContextHolder.getLocale());
        currencyFormatter.setMaximumFractionDigits(0);
        return currencyFormatter.format(orderDao.getTotalSales(writerId));
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getTotalSalesForBook(long bookId){
        return orderDao.getTotalSalesForBook(bookId);
    }


    @Transactional(readOnly = true)
    @Override
    public String getTotalSalesForMonth(long writerId, int year, int month){
        BigDecimal sales = orderDao.getTotalSalesForMonth(writerId, year, month);
        if(sales == null){
            sales=BigDecimal.valueOf(0);
        }
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(LocaleContextHolder.getLocale());
        currencyFormatter.setMaximumFractionDigits(0);
        return currencyFormatter.format(sales);
    }

    @Transactional(readOnly = true)
    @Override
    public String getSalesIncrease(long writerId) {
        BigDecimal thisMonth = orderDao.getTotalSalesForMonth(writerId, YearMonth.now().getYear(), YearMonth.now().getMonthValue());
        BigDecimal lastMonth = orderDao.getTotalSalesForMonth(writerId, YearMonth.now().minusMonths(1).getYear(), YearMonth.now().minusMonths(1).getMonthValue());

        if(thisMonth==null){
            thisMonth=BigDecimal.valueOf(0);
        }
        if(lastMonth==null){
            lastMonth=BigDecimal.valueOf(0);
        }
        if (lastMonth.equals(BigDecimal.ZERO)) {
            return "";
        }

        BigDecimal change = thisMonth.subtract(lastMonth);
        BigDecimal percentageChange = change.divide(lastMonth, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        return String.format("%+d%%", percentageChange.intValue());
    }

    @Transactional(readOnly = true)
    @Override
    public String getOrdersIncrease(long writerId) {
        long thisMonth = orderDao.getTotalOrdersForMonthForWriter(writerId, YearMonth.now().getYear(), YearMonth.now().getMonthValue());
        long lastMonth = orderDao.getTotalOrdersForMonthForWriter(writerId, YearMonth.now().minusMonths(1).getYear(), YearMonth.now().minusMonths(1).getMonthValue());

        if (lastMonth == 0) {
            return "0";
        }

        long change = thisMonth - lastMonth;
        double percentageChange = (double) change / lastMonth * 100;

        return String.format("%+d%%", (int) Math.round(percentageChange));

    }



    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<AnalyticsBook> getBooksByWriterWithAnalytics(long writerId, boolean byMonths, int month, int year, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books;
        List<AnalyticsBook> analyticsBooks;
        PaginatedContent<AnalyticsBook> page;
        if(!byMonths){
            books = orderDao.getBooksByWriterOrderedBySales(writerId, (pageNumber-1)*pageSize, pageSize);
            analyticsBooks = books.stream()
                    .map(book -> new AnalyticsBook(book, getTotalOrdersForBook(book.getBookId()), getTotalSalesForBook(book.getBookId())))
                    .toList();
            page = new PaginatedContent<>(analyticsBooks, pageNumber, pageSize, orderDao.getBooksByWriterOrderedSize(writerId));
        }
        else{
            books = orderDao.getBooksByWriterOrderedBySales(writerId, (pageNumber-1)*pageSize, pageSize, year, month);
            analyticsBooks = books.stream()
                    .map(book -> new AnalyticsBook(book, orderDao.getTotalOrdersForMonthForBook(book.getBookId(), year, month), orderDao.getTotalSalesForMonthForBook(book.getBookId(), year, month)))
                    .toList();
            page = new PaginatedContent<>(analyticsBooks, pageNumber, pageSize, orderDao.getBooksByWriterOrderedSize(writerId, year, month));
        }

        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getBooksByWriterWithAnalytics(writerId, byMonths, month, year, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }

    @Override
    public List<Integer> getYears(){
        int currentYear = YearMonth.now().getYear();
        List<Integer> years = new ArrayList<>();
        for (int year = 2024; year <= currentYear; year++) {
            years.add(year);
        }
        return years;
    }

    @Override
    public List<Integer> getMonths(){
        List<Integer> months = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
        return months;
    }

}

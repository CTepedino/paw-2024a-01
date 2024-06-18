package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.AnalyticsBook;
import ar.edu.itba.paw.models.books.Book;

import java.math.BigDecimal;
import java.util.List;

public interface AnalyticsService {

    Long getTotalOrdersForWriter(long writerId);

    Long getTotalOrdersForBook(long bookId);

    String getTotalSales(long writerId);

    BigDecimal getTotalSalesForBook(long bookId);

    String getTotalSalesForMonth(long writerId, int year, int month);

    Long getTotalOrdersForWriterForMonth(long writerId, int year, int month);

    String getSalesIncrease(long writerId);

    String getOrdersIncrease(long writerId);

    List<Book> getTop5BooksByWriter(long writerId);

    List<AnalyticsBook> getBooksByWriterWithAnalytics(long writerId, boolean byMonths, int month, int year, int page, int pageSize);

    List<Integer> getYears();

    List<Integer> getMonths();

}

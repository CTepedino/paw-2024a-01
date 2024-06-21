package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.AnalyticsBook;
import ar.edu.itba.paw.models.books.Book;

import java.math.BigDecimal;
import java.util.List;

public interface AnalyticsService {

    long getTotalOrdersForWriter(long writerId);

    long getTotalOrdersForBook(long bookId);

    String getTotalSales(long writerId);

    BigDecimal getTotalSalesForBook(long bookId);

    String getTotalSalesForMonth(long writerId, int year, int month);

    long getTotalOrdersForWriterForMonth(long writerId, int year, int month);

    String getSalesIncrease(long writerId);

    String getOrdersIncrease(long writerId);

    PaginatedContent<AnalyticsBook> getBooksByWriterWithAnalytics(long writerId, boolean byMonths, int month, int year, int pageNumber, int pageSize);

    List<Integer> getYears();

    List<Integer> getMonths();

}

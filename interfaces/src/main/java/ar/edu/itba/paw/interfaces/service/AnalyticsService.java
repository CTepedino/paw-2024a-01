package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.AnalyticsBook;
import ar.edu.itba.paw.models.books.Book;

import java.math.BigDecimal;
import java.util.List;

public interface AnalyticsService {

    Long getTotalOrdersForWriter(long writerId);

    Long getTotalOrdersForBook(long bookId);

    BigDecimal getTotalSales(long writerId);

    BigDecimal getTotalSalesForBook(long bookId);

    BigDecimal getTotalSalesForMonth(long writerId, int year, int month);

    List<Book> getTop5BooksByWriter(long writerId);

    List<AnalyticsBook> getBooksByWriterWithAnalytics(long writerId);

}

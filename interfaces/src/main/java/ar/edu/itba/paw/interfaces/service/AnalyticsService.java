package ar.edu.itba.paw.interfaces.service;

import java.math.BigDecimal;

public interface AnalyticsService {

    BigDecimal getTotalSalesForWriterForMonth(long writerId, int year, int month);
    long getTotalOrdersForWriterForMonth(long writerId, int year, int month);


    long getTotalOrdersForBookForMonth(long bookId, int year, int month);
    BigDecimal getTotalSalesForBookForMonth(long bookId, int year, int month);

}

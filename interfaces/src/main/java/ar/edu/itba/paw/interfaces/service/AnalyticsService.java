package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.BookAnalytics;
import ar.edu.itba.paw.models.users.UserAnalytics;

import java.math.BigDecimal;
import java.time.YearMonth;

public interface AnalyticsService {

    UserAnalytics getUserAnalytics(long userId, YearMonth yearMonth);
    BookAnalytics getBookAnalytics(long bookId, YearMonth yearMonth);
}

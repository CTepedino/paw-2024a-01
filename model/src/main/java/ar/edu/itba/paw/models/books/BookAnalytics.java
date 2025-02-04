package ar.edu.itba.paw.models.books;

import ar.edu.itba.paw.models.Analytics;

import java.math.BigDecimal;
import java.time.YearMonth;

public class BookAnalytics extends Analytics {
    private final long bookId;

    public BookAnalytics(long bookId, long orderCount, BigDecimal totalSales, YearMonth period) {
        super(orderCount, totalSales, period);
        this.bookId = bookId;
    }

    public long getBookId() {
        return bookId;
    }
}

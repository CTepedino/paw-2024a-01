package ar.edu.itba.paw.models.books;

import ar.edu.itba.paw.models.Analytics;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BookAnalytics extends Analytics {
    private final long bookId;

    public BookAnalytics(long bookId, long orderCount, BigDecimal totalSales) {
        super(orderCount, totalSales);
        this.bookId = bookId;
    }

    public long getBookId() {
        return bookId;
    }
}

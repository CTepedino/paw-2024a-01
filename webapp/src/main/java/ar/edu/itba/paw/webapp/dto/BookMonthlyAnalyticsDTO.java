package ar.edu.itba.paw.webapp.dto;

import java.math.BigDecimal;
import java.net.URI;

public class BookMonthlyAnalyticsDTO {

    private long bookId;
    private long orderCount;
    private BigDecimal salesTotal;

    private URI book;
    private URI previousMonth;

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(BigDecimal salesTotal) {
        this.salesTotal = salesTotal;
    }

    public URI getBook() {
        return book;
    }

    public void setBook(URI book) {
        this.book = book;
    }

    public URI getPreviousMonth() {
        return previousMonth;
    }

    public void setPreviousMonth(URI previousMonth) {
        this.previousMonth = previousMonth;
    }
}

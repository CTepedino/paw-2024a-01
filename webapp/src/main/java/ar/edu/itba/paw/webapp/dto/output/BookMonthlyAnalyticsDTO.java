package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.books.BookAnalytics;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;

public class BookMonthlyAnalyticsDTO {

    private long bookId;
    private long orderCount;
    private BigDecimal salesTotal;

    private URI book;

    public static BookMonthlyAnalyticsDTO fromAnalytics(UriInfo uriInfo, BookAnalytics a){
        BookMonthlyAnalyticsDTO dto = new BookMonthlyAnalyticsDTO();

        dto.bookId = a.getBookId();
        dto.orderCount = a.getOrderCount();
        dto.salesTotal = a.getTotalSales();

        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(dto.bookId)).build();

        return dto;
    }

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

}

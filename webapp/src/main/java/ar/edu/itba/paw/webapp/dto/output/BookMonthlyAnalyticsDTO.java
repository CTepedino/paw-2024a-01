package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.books.BookAnalytics;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.time.YearMonth;

public class BookMonthlyAnalyticsDTO {

    private long bookId;
    private long orderCount;
    private BigDecimal salesTotal;
    private YearMonth month;

    private URI self;
    private URI book;
    private URI nextMonth;
    private URI prevMonth;

    public static BookMonthlyAnalyticsDTO fromAnalytics(UriInfo uriInfo, BookAnalytics a){
        BookMonthlyAnalyticsDTO dto = new BookMonthlyAnalyticsDTO();

        dto.bookId = a.getBookId();
        dto.orderCount = a.getOrderCount();
        dto.salesTotal = a.getTotalSales();
        dto.month = a.getPeriod();

        dto.self = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(dto.bookId)).path("monthly_analytics").path(dto.month.toString()).build();
        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(dto.bookId)).build();
        dto.prevMonth = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(dto.bookId)).path("monthly_analytics")
                .path(dto.month.minusMonths(1).toString()).build();
        if (!a.getPeriod().equals(YearMonth.now())){
            dto.nextMonth = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(dto.bookId)).path("monthly_analytics")
                    .path(dto.month.plusMonths(1).toString()).build();
        }
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

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getNextMonth() {
        return nextMonth;
    }

    public void setNextMonth(URI nextMonth) {
        this.nextMonth = nextMonth;
    }

    public URI getPrevMonth() {
        return prevMonth;
    }

    public void setPrevMonth(URI prevMonth) {
        this.prevMonth = prevMonth;
    }
}

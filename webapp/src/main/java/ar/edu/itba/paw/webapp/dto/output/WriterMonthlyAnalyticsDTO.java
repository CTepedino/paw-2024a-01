package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.users.UserAnalytics;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.time.YearMonth;

public class WriterMonthlyAnalyticsDTO {

    private long writerId;
    private long orderCount;
    private BigDecimal salesTotal;
    private YearMonth month;

    private URI self;
    private URI writer;
    private URI nextMonth;
    private URI prevMonth;

    public static WriterMonthlyAnalyticsDTO fromAnalytics(UriInfo uriInfo, UserAnalytics a){
        WriterMonthlyAnalyticsDTO dto = new WriterMonthlyAnalyticsDTO();

        dto.writerId = a.getUserId();
        dto.orderCount = a.getOrderCount();
        dto.salesTotal = a.getTotalSales();
        dto.month = a.getPeriod();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.writerId)).path("monthly-analytics").path(dto.month.toString()).build();
        dto.writer = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.writerId)).build();
        dto.prevMonth = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.writerId)).path("monthly-analytics")
                .path(dto.month.minusMonths(1).toString()).build();
        if (!a.getPeriod().equals(YearMonth.now())){
            dto.nextMonth = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.writerId)).path("monthly-analytics")
                    .path(dto.month.plusMonths(1).toString()).build();
        }
        return dto;
    }

    public long getWriterId() {
        return writerId;
    }

    public void setWriterId(long writerId) {
        this.writerId = writerId;
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

    public URI getWriter() {
        return writer;
    }

    public void setWriter(URI writer) {
        this.writer = writer;
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
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

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}

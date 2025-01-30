package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.users.UserAnalytics;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;

public class WriterMonthlyAnalyticsDTO {

    private long writerId;
    private long orderCount;
    private BigDecimal salesTotal;

    private URI writer;

    public static WriterMonthlyAnalyticsDTO fromAnalytics(UriInfo uriInfo, UserAnalytics a){
        WriterMonthlyAnalyticsDTO dto = new WriterMonthlyAnalyticsDTO();

        dto.writerId = a.getUserId();
        dto.orderCount = a.getOrderCount();
        dto.salesTotal = a.getTotalSales();

        dto.writer = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.writerId)).build();

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

}

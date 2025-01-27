package ar.edu.itba.paw.webapp.dto;

import java.net.URI;

public class WriterMonthlyAnalyticsDTO {

    private long writerId;
    private long orderCount;
    private long salesTotal;

    private URI writer;
    private URI previousMonth;

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

    public long getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(long salesTotal) {
        this.salesTotal = salesTotal;
    }

    public URI getWriter() {
        return writer;
    }

    public void setWriter(URI writer) {
        this.writer = writer;
    }

    public URI getPreviousMonth() {
        return previousMonth;
    }

    public void setPreviousMonth(URI previousMonth) {
        this.previousMonth = previousMonth;
    }
}

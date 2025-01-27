package ar.edu.itba.paw.models;

import java.math.BigDecimal;

public abstract class Analytics {
    private final long orderCount;
    private final BigDecimal totalSales;

    public Analytics(long orderCount, BigDecimal totalSales) {
        this.orderCount = orderCount;
        this.totalSales = totalSales;
    }


    public long getOrderCount() {
        return orderCount;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }
}

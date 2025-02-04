package ar.edu.itba.paw.models;

import java.math.BigDecimal;
import java.time.YearMonth;

public abstract class Analytics {
    private final long orderCount;
    private final BigDecimal totalSales;
    private final YearMonth period;

    public Analytics(long orderCount, BigDecimal totalSales, YearMonth period) {
        this.orderCount = orderCount;
        this.totalSales = totalSales;
        this.period = period;
    }


    public long getOrderCount() {
        return orderCount;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public YearMonth getPeriod() {
        return period;
    }
}

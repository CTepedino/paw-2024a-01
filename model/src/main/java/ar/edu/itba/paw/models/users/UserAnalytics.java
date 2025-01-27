package ar.edu.itba.paw.models.users;

import ar.edu.itba.paw.models.Analytics;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UserAnalytics extends Analytics {
    private final long userId;

    public UserAnalytics(long userId, long orderCount, BigDecimal totalSales) {
        super(orderCount, totalSales);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}

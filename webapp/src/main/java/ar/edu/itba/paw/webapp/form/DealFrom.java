package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class DealFrom {

    @NotNull
    @PositiveOrZero
    @DecimalMax(value = "100000000.0")
    private BigDecimal price;

    @NotNull
    @PositiveOrZero
    private Integer duration;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


}

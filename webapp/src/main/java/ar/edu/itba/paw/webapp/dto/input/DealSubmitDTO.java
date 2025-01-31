package ar.edu.itba.paw.webapp.dto.input;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class DealSubmitDTO {

    @NotNull
    @PositiveOrZero
    @DecimalMax(value = "100000000.0")
    private BigDecimal price;

    @Positive
    @Max(value = 100)
    private int duration;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

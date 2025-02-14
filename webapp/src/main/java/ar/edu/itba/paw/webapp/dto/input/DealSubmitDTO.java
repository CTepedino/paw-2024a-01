package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.dto.input.validations.TomorrowOrMore;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DealSubmitDTO {

    @NotNull
    @PositiveOrZero
    @DecimalMax(value = "100000000.0")
    private BigDecimal price;

    @TomorrowOrMore
    @NotNull
    private LocalDate end;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}

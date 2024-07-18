package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.DealPriceValid;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@DealPriceValid
public class DealForm {

    @NotNull
    @PositiveOrZero
    @DecimalMax(value = "100000000.0")
    private BigDecimal price;

    @NotNull
    @Positive
    @Max(value = 100)
    private Integer duration;

    private BigDecimal bookPrice;

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(BigDecimal bookPrice) {
        this.bookPrice = bookPrice;
    }

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

package ar.edu.itba.paw.webapp.form.validations;

import ar.edu.itba.paw.webapp.form.DealForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DealPriceValidator implements ConstraintValidator<DealPriceValid, DealForm> {

    @Override
    public void initialize(DealPriceValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(DealForm dealForm, ConstraintValidatorContext context) {
        if (dealForm == null) {
            return true;
        }

        BigDecimal dealPrice = dealForm.getPrice();
        BigDecimal bookPrice = dealForm.getBookPrice();

        if (dealPrice == null || bookPrice == null){
            return false;
        }

        if (dealPrice.compareTo(bookPrice.multiply(new BigDecimal("0.95").setScale(2, RoundingMode.HALF_UP))) >= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{DealPriceValid}")
                    .addPropertyNode("price")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}


package ar.edu.itba.paw.webapp.form.validations;

import ar.edu.itba.paw.webapp.form.DealFrom;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class DealPriceValidator implements ConstraintValidator<DealPriceValid, DealFrom> {

    @Override
    public void initialize(DealPriceValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(DealFrom dealForm, ConstraintValidatorContext context) {
        if (dealForm == null) {
            return true; // Handled by @NotNull annotation on the class field.
        }

        BigDecimal dealPrice = dealForm.getPrice();
        BigDecimal bookPrice = dealForm.getBookPrice();

        if (dealPrice != null && bookPrice != null && dealPrice.compareTo(bookPrice) >= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{DealPriceValid}")
                    .addPropertyNode("price")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}


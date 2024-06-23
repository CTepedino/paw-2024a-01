package ar.edu.itba.paw.webapp.form.validations;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.Temporal;

public class TodayOrBeforeValidator implements ConstraintValidator<TodayOrBefore, Temporal> {

    @Override
    public boolean isValid(Temporal o, ConstraintValidatorContext constraintValidatorContext) {
        return o == null || LocalDate.from(o).isBefore(LocalDate.now()) || LocalDate.from(o).isEqual(LocalDate.now());
    }

    @Override
    public void initialize(TodayOrBefore constraintAnnotation) {}
}

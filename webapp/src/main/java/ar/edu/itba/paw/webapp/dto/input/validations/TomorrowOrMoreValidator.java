package ar.edu.itba.paw.webapp.dto.input.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.Temporal;

public class TomorrowOrMoreValidator implements ConstraintValidator<TomorrowOrMore, Temporal> {

    @Override
    public boolean isValid(Temporal o, ConstraintValidatorContext constraintValidatorContext) {
        return o == null || LocalDate.from(o).isAfter(LocalDate.now());
    }

    @Override
    public void initialize(TomorrowOrMore constraintAnnotation) {}
}

package ar.edu.itba.paw.webapp.dto.input.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = TomorrowOrMoreValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TomorrowOrMore {
    String message() default "The date has to be at least 24 hours from now";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

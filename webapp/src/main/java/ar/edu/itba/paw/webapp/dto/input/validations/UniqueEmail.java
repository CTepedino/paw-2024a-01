package ar.edu.itba.paw.webapp.dto.input.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UniqueEmail {

    String message() default "Email already in use";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

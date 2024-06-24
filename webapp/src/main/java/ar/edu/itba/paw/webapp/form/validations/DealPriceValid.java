package ar.edu.itba.paw.webapp.form.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DealPriceValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DealPriceValid {
    String message() default "El precio del deal no puede ser mayor o igual al precio del libro";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

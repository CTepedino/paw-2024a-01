package ar.edu.itba.paw.webapp.dto.input.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = ImageOrPdfValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImageOrPdf {
    String message() default "Please submit an image or pdf";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

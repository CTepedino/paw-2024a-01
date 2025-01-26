package ar.edu.itba.paw.webapp.form.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = PdfOrImageFileValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PdfOrImageFile {

    String message() default "Please submit an image or pdf";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

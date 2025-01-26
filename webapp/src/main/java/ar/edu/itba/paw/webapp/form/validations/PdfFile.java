package ar.edu.itba.paw.webapp.form.validations;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = PdfFileValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PdfFile {

    String message() default "Please submit a pdf";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

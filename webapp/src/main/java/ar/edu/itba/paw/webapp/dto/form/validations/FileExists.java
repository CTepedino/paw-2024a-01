package ar.edu.itba.paw.webapp.dto.form.validations;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = FileExistsValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FileExists {

    String message() default "Please submit a file";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

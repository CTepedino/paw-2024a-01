package ar.edu.itba.paw.webapp.form.validations;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.*;


public class PdfOrImageFileValidator implements ConstraintValidator<PdfOrImageFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || s.isEmpty() || s.getContentType()==null || s.getContentType().toLowerCase().startsWith("image") || s.getContentType().equalsIgnoreCase("application/pdf");
    }

    @Override
    public void initialize(PdfOrImageFile constraintAnnotation) {}
}

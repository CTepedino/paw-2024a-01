package ar.edu.itba.paw.webapp.form.validations;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PdfFileValidator implements ConstraintValidator<PdfFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile s, ConstraintValidatorContext constraintValidatorContext) {
        return s.isEmpty() || s.getContentType()==null || s.getContentType().equals("application/pdf");
    }

    @Override
    public void initialize(PdfFile constraintAnnotation) {}
}

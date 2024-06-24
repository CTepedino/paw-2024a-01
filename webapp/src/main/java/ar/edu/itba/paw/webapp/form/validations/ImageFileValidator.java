package ar.edu.itba.paw.webapp.form.validations;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {


    @Override
    public boolean isValid(MultipartFile s, ConstraintValidatorContext constraintValidatorContext) {
        return s==null || s.isEmpty() || s.getContentType()==null || s.getContentType().toLowerCase().startsWith("image/");
    }

    @Override
    public void initialize(ImageFile constraintAnnotation) {}
}

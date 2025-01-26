package ar.edu.itba.paw.webapp.form.validations;


import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileExistsValidator implements ConstraintValidator<FileExists, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile s, ConstraintValidatorContext constraintValidatorContext) {
        return s.getSize() > 0;
    }

    @Override
    public void initialize(FileExists constraintAnnotation) {}
}

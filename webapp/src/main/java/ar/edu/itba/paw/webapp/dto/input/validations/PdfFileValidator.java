package ar.edu.itba.paw.webapp.dto.input.validations;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PdfFileValidator implements ConstraintValidator<PdfFile, FormDataBodyPart> {

    @Override
    public boolean isValid(FormDataBodyPart s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s.getMediaType().toString().equalsIgnoreCase("application/pdf");
    }

    @Override
    public void initialize(PdfFile constraintAnnotation) {}
}

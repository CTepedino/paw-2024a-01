package ar.edu.itba.paw.webapp.dto.input.validations;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageOrPdfValidator implements ConstraintValidator<ImageOrPdf, FormDataBodyPart> {

    @Override
    public boolean isValid(FormDataBodyPart s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && (s.getMediaType().toString().toLowerCase().startsWith("image/") || s.getMediaType().toString().equalsIgnoreCase("application/pdf"));
    }

}

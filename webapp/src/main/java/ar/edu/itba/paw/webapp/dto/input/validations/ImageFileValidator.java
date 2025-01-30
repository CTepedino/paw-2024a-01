package ar.edu.itba.paw.webapp.dto.input.validations;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ImageFile, FormDataBodyPart> {

    @Override
    public boolean isValid(FormDataBodyPart s, ConstraintValidatorContext constraintValidatorContext){
        return s != null && s.getMediaType().toString().toLowerCase().startsWith("image/");
    }
}

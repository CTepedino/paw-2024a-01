package ar.edu.itba.paw.webapp.dto.input.validations;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;

public class ImageFileValidator implements ConstraintValidator<ImageFile, FormDataBodyPart> {

    @Override
    public boolean isValid(FormDataBodyPart s, ConstraintValidatorContext constraintValidatorContext){
        return s != null && s.getMediaType().toString().toLowerCase().startsWith("image/");
    }
}

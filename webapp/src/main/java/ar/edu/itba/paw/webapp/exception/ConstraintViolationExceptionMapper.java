package ar.edu.itba.paw.webapp.exception;

import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Component
@Provider
@Singleton
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<String> errors = new ArrayList<>();

        e.getConstraintViolations()
                .forEach(cv -> errors.add(formatErrorMessage(cv)));

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new GenericEntity<>(errors) {})
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private String formatErrorMessage(ConstraintViolation<?> cv){
        String param = "";
        for (Path.Node path : cv.getPropertyPath()){
            param = path.toString();
        }
        return String.format("%s : %s", param, cv.getMessage());
    }
}

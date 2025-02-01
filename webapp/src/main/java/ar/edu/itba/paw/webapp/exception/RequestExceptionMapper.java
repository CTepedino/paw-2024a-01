package ar.edu.itba.paw.webapp.exception;

import ar.edu.itba.paw.models.exception.RequestException;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Provider
@Component
public class RequestExceptionMapper implements ExceptionMapper<RequestException> {

    @Override
    public Response toResponse(RequestException e) {
        return Response
                .status(e.getStatusCode())
                .build();
    }
}

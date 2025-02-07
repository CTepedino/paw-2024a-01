package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.dto.input.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("validation_codes")
@Component
public class ValidationCodeController {

    @Autowired
    private UserService us;

    @Context
    private UriInfo uriInfo;

    @POST
    public Response resendEmailValidationCode(@Valid final EmailDTO emailDTO){
        us.resendValidation(emailDTO.getEmail());
        return Response.noContent().build();
    }
}

package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    @Path("{email}")
    @GET
    public Response resendCode(@PathParam("email") final String email){
        us.resendValidation(email);
        return Response.ok().build();
    }
}

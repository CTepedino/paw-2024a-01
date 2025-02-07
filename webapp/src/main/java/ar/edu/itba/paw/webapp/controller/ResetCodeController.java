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

@Component
@Path("reset_password_codes")
public class ResetCodeController {

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @POST
    public Response sendResetPasswordCodeEmail(@Valid final EmailDTO emailDTO){
        userService.sendResetCode(emailDTO.getEmail());
        return Response.ok().build();
    }

}

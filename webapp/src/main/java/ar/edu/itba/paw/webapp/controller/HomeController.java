package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.contentType.VndMediaTypes;
import ar.edu.itba.paw.webapp.dto.output.IndexDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
@Component
public class HomeController {

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {VndMediaTypes.INDEX})
    public Response index(){
        return Response.ok(IndexDTO.create(uriInfo, userService.getLoggedUser())).build();
    }
}

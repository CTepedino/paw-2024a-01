package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("users")
@Component
public class UserController {

    @Autowired
    private UserService us;

    @Context
    private UriInfo uriInfo;


    @GET
    @Produces(value = {MediaType.APPLICATION_JSON}) //TODO: custom
    public Response listUsers(@QueryParam("page") @DefaultValue("1") final int page){
        final List<UserDTO> allUsers = Stream.of(us.findById(1).get()).map(UserDTO.mapper(uriInfo)).toList();

        return Response.ok(new GenericEntity<List<UserDTO>>(allUsers) {})
                .link(URI.create(""), "prev").link(URI.create(""), "next")
                .link(URI.create(""), "first").link(URI.create(""), "last")
                .build();
        //Response.ok().cacheControl(...)
        //.header(...)
        //.link(URI uri, String rel)
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response createUser(final UserDTO userDto){
        final User user = us.create(userDto.getEmail(), userDto.getPassword(), userDto.getFirstName(), userDto.getLastName());

        final URI uri = uriInfo.getAbsolutePathBuilder().path("users").path(String.valueOf(user.getUserId())).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") final long id){
        final Optional<User> user = us.findById(id);

        if (user.isPresent()){
            return Response.ok(UserDTO.fromUser(uriInfo, user.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") final long id){
        //us.deleteById(id);
        return Response.noContent().build();
    }
}

package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserAnalytics;
import ar.edu.itba.paw.webapp.dto.input.PasswordEditDTO;
import ar.edu.itba.paw.webapp.dto.input.UserCreateDTO;
import ar.edu.itba.paw.webapp.dto.input.UserEditDTO;
import ar.edu.itba.paw.webapp.dto.input.validations.ImageFile;
import ar.edu.itba.paw.webapp.dto.output.UserDTO;
import ar.edu.itba.paw.webapp.dto.output.WriterMonthlyAnalyticsDTO;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Path("users")
@Component
public class UserController { //TODO: exceptions. custom mime types

    private final UserService us;
    private final AnalyticsService as;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(final UserService us, final AnalyticsService as){
        this.us = us;
        this.as = as;
    }

    @POST
    @Produces(value = MediaType.APPLICATION_JSON)
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response createUser(@Valid final UserCreateDTO userDto){
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

    @PUT
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response modifyUser(
            @PathParam("id") final long id,
            @Valid UserEditDTO userDTO
    ){
        final Optional<User> user = us.findById(id);

        if (user.isPresent()){
            us.updateProfile(id, userDTO.getFirstName(), userDTO.getLastName(), userDTO.getCbu(), userDTO.getDescription());
            return Response.ok(UserDTO.fromUser(uriInfo, user.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}/password")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response modifyPassword(
            @PathParam("id") final long id,
            @Size(min = 6, max = 255) @FormDataParam("password") String password
    ){
        us.changePassword(id, password);
    }

    @GET
    @Path("/{id}/profile_picture")
    @Produces(value = {"image/jpeg", "image/png"})
    public Response getProfilePicture(@PathParam("id") final long id){
        ProfilePicture image = us.getProfilePicture(id);

        return Response.ok(image.getFile(), "image/jpeg").build(); //TODO: downsizing y cache
    }

    @PUT
    @Path("/{id}/profile_picture")
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response setProfilePicture(
            @PathParam("id") final long id,
            @ImageFile @FormDataParam("image")  final FormDataBodyPart image
    ){
        us.updateProfilePicture(id, image.getEntityAs(byte[].class));
        return Response
                .ok()
                .contentLocation(uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(id)).path("profile_picture").build())
                .build();
    }

    @DELETE
    @Path("/{id}/profile_picture")
    public Response deleteProfilePicture(@PathParam("id") final long id){
        us.deleteProfilePicture(id);
        return Response.noContent().build();
    }

    @GET
    @Path("{user_id}/monthly_analytics/{year_month:\\d{4}-\\d{2}}") //yyyy-MM:
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getMonthlyWriterAnalytics(
            @PathParam("user_id") final long userId,
            @PathParam("year_month") final String period
    ){

        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.parse(period);
        } catch (DateTimeParseException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        UserAnalytics analytics = as.getUserAnalytics(userId, yearMonth);
        return Response.ok(WriterMonthlyAnalyticsDTO.fromAnalytics(uriInfo, analytics)).build();
    }


}

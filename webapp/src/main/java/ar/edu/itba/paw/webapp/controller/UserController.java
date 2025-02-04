package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserAnalytics;
import ar.edu.itba.paw.webapp.contentType.VndMediaTypes;
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

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.fileResponse;

@Path("users")
@Component
public class UserController {

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
    @Consumes(value = VndMediaTypes.USER)
    public Response createUser(
            @Valid final UserCreateDTO userDto,
            @Context HttpHeaders headers
    ){
        final User user = us.create(userDto.getEmail(), userDto.getPassword(), userDto.getFirstName(), userDto.getLastName(), headers.getAcceptableLanguages().getFirst());

        return Response
                .created(uriInfo.getAbsolutePathBuilder().path("users").path(String.valueOf(user.getUserId())).build())
                .build();
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces(value = {VndMediaTypes.USER})
    public Response getById(@PathParam("id") final long id){

        final User user = us.findById(id).orElseThrow(UserNotFoundException::new);

        return Response.ok(UserDTO.fromUser(uriInfo, user)).build();
    }

    @PUT
    @Path("/{id:\\d+}")
    @Consumes(value = {VndMediaTypes.USER})
    public Response modifyUser(
            @PathParam("id") final long id,
            @Valid UserEditDTO userDTO
    ){
        us.updateProfile(id, userDTO.getFirstName(), userDTO.getLastName(), userDTO.getCbu(), userDTO.getDescription());

        return Response.noContent().build();
    }

    @PUT
    @Path("/{id:\\d+}/password")
    @Consumes(value = {VndMediaTypes.PASSWORD})
    public Response modifyPassword(
            @PathParam("id") final long id,
            @Valid PasswordEditDTO passwordEditDTO
    ){
        us.changePassword(id, passwordEditDTO.getPassword());

        return Response.ok().build();
    }

    @GET
    @Path("/{id:\\d+}/profile_picture")
    @Produces(value = {"image/jpeg"})
    public Response getProfilePicture(@PathParam("id") final long id){
        ProfilePicture image = us.getProfilePicture(id);

        return fileResponse(image, "image/jpeg").build(); //TODO: downsizing y cache
    }

    @PUT
    @Path("/{id:\\d+}/profile_picture")
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response setProfilePicture(
            @PathParam("id") final long id,
            @ImageFile @FormDataParam("image")  final FormDataBodyPart image
    ){
        us.updateProfilePicture(id, image.getEntityAs(byte[].class));
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id:\\d+}/profile_picture")
    public Response deleteProfilePicture(@PathParam("id") final long id){
        us.deleteProfilePicture(id);
        return Response.noContent().build();
    }

    @GET
    @Path("{user_id:\\d+}/monthly_analytics/{year_month:\\d{4}-\\d{2}}") //yyyy-MM
    @Produces(value = {VndMediaTypes.WRITER_ANALYTICS})
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

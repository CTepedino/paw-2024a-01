package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Recommendation;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserAnalytics;
import ar.edu.itba.paw.webapp.dto.input.UserCreateDTO;
import ar.edu.itba.paw.webapp.dto.input.UserEditDTO;
import ar.edu.itba.paw.webapp.dto.input.validations.ImageFile;
import ar.edu.itba.paw.webapp.dto.output.RecommendationDTO;
import ar.edu.itba.paw.webapp.dto.output.UserDTO;
import ar.edu.itba.paw.webapp.dto.output.WriterMonthlyAnalyticsDTO;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.fileResponse;
import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("users")
@Component
public class UserController { //TODO: exceptions. custom mime types

    private final UserService us;
    private final BookService bs;
    private final AnalyticsService as;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(final UserService us, final BookService bs, final AnalyticsService as){
        this.us = us;
        this.bs = bs;
        this.as = as;
    }

    @POST
    @Produces(value = MediaType.APPLICATION_JSON)
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response createUser(@Valid final UserCreateDTO userDto){ //TODO: status code on repeated email
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
            return Response.ok(UserDTO.fromUser(uriInfo, user.get())).build(); //TODO: return model on PUT?
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}/password")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response modifyPassword(
            @PathParam("id") final long id,
            @Valid String password
    ){
        us.changePassword(id, password);

        return Response.ok().build();
    }

    @GET
    @Path("/{id}/profile_picture")
    @Produces(value = {"image/jpeg", "image/png"})
    public Response getProfilePicture(@PathParam("id") final long id){
        ProfilePicture image = us.getProfilePicture(id);

        return fileResponse(image, "image/jpeg").build(); //TODO: downsizing y cache
    }

    @PUT
    @Path("/{id}/profile_picture")
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response setProfilePicture(
            @PathParam("id") final long id,
            @ImageFile @FormDataParam("image")  final FormDataBodyPart image
    ){
        us.updateProfilePicture(id, image.getEntityAs(byte[].class));
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}/profile_picture")
    public Response deleteProfilePicture(@PathParam("id") final long id){
        us.deleteProfilePicture(id);
        return Response.noContent().build();
    }

    @GET
    @Path("{user_id}/monthly_analytics/{year_month:\\d{4}-\\d{2}}") //yyyy-MM
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

    @GET
    @Path("{id}/recommendations")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRecommendations(
            @PathParam("id") final long userId,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("size") @DefaultValue("20") final int size

    ){
        PaginatedContent<Recommendation> recommendationPage = bs.getRecommendations(userId, page, size);
        List<RecommendationDTO> recommendations = recommendationPage.getPage().stream().map(RecommendationDTO.mapper(uriInfo)).toList();
        return paginatedResponse(
                Response.ok(new GenericEntity<>(recommendations){}), recommendationPage, uriInfo
        ).build();
    }

    @GET
    @Path("{userId}/recommendations/{bookId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRecommendation(
            @PathParam("userId") final long userId,
            @PathParam("bookId") final long bookId
    ){
        Optional<Recommendation> recommendation = bs.findRecommendation(userId, bookId);
        if(recommendation.isPresent()){
            return Response.ok(RecommendationDTO.fromRecommendation(uriInfo, recommendation.get())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("{userId}/recommendations")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response addRecommendation(
            @PathParam("userId") final long userId,
            @FormDataParam("bookId") final long bookId
    ){
        bs.recommend(userId, bookId);

        return Response
                .created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(bookId)).build())
                .build();
    }

    @DELETE
    @Path("{userId}/recommendations/{bookId}")
    public Response removeRecommendation(
            @PathParam("userId") final long userId,
            @PathParam("bookId") final long bookId
    ){
        bs.removeFromWishlist(userId, bookId);

        return Response.noContent().build();
    }

}

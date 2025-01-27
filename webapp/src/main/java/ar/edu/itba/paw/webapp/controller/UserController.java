package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.WishlistItem;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserAnalytics;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import ar.edu.itba.paw.webapp.dto.WishlistDTO;
import ar.edu.itba.paw.webapp.dto.WriterMonthlyAnalyticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

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

/*    @POST
    @Produces(value = MediaType.APPLICATION_JSON)
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response createUser(@Valid final UserPostDTO userDto){
        final User user = us.create(userDto.getEmail(), userDto.getPassword(), userDto.getFirstName(), userDto.getLastName());
        final URI uri = uriInfo.getAbsolutePathBuilder().path("users").path(String.valueOf(user.getUserId())).build();
        return Response.created(uri).build();
    }*/

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

/*    @PUT
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response modifyUser(
            @PathParam("id") final long id,
            @Valid UserPutDTO userDTO
    ){
        final Optional<User> user = us.findById(id);

        if (user.isPresent()){
            us.updateProfile(id, userDTO.getFirstName(), userDTO.getLastName(), userDTO.getCbu(), userDTO.getDescription());
            return Response.ok(UserDTO.fromUser(uriInfo, user.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }*/


    @GET
    @Path("/{id}/profilePicture")
    @Produces(value = {"image/jpeg", "image/png"})
    public Response getProfilePicture(@PathParam("id") final long id){
        ProfilePicture image = us.getProfilePicture(id);

        return Response.ok(image.getFile()).build();
    }

    @GET
    @Path("{userId}/wishlist")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getWishlist(
            @PathParam("userId") final long id,
            @QueryParam("page") final int page,
            @QueryParam("size") final int size
    ){
        PaginatedContent<WishlistItem> wishlistPage = bs.getWishlist(id, page, size);
        List<WishlistDTO> wishlist = wishlistPage.getPage().stream().map(WishlistDTO.mapper(uriInfo)).toList();
        return paginatedResponse(
                Response.ok(new GenericEntity<>(wishlist){}), wishlistPage, uriInfo
        ).build();
    }

    @GET
    @Path("{userId}/wishlist/{bookId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getWishlistItem(
            @PathParam("userId") final long userId,
            @PathParam("bookId") final long bookId
    ){
        Optional<WishlistItem> wishlistItem = bs.findWishlistItem(userId, bookId);
        if (wishlistItem.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(wishlistItem).build();
    }

    @GET
    @Path("{user_id}/monthly_analytics/{year_month: \\d{4}-\\d{2}}}") //yyyy-MM
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getMonthlyWriterAnalytics(
            @PathParam("user_id") final long userId,
            @PathParam("year_month") final String period
    ){
        //TODO -> on the service
        YearMonth yearMonth = YearMonth.parse(period);


        UserAnalytics analytics = new UserAnalytics(
                userId,
                as.getTotalOrdersForWriterForMonth(userId, yearMonth.getYear(), yearMonth.getMonthValue()),
                as.getTotalSalesForWriterForMonth(userId, yearMonth.getYear(), yearMonth.getMonthValue())
        );

        return Response.ok(WriterMonthlyAnalyticsDTO.fromAnalytics(uriInfo, analytics)).build();
    }

}

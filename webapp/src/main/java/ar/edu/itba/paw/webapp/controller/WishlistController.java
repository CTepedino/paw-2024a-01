package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.WishlistItem;
import ar.edu.itba.paw.webapp.dto.WishlistDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("/users/{userId}")
public class WishlistController {

    private final BookService bs;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public WishlistController(final BookService bs){
        this.bs = bs;
    }

    @GET
    @Path("wishlist")
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
    @Path("wishlist/{bookId}")
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

}

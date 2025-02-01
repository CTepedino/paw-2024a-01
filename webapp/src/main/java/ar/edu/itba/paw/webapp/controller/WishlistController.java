package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.WishlistItem;
import ar.edu.itba.paw.webapp.dto.input.WishlistCreateDTO;
import ar.edu.itba.paw.webapp.dto.output.WishlistDTO;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("users/{userId}/wishlist")
@Component
public class WishlistController {

    private final BookService bs;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public WishlistController(final BookService bs){
        this.bs = bs;
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getWishlist(
            @PathParam("userId") final long id,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("size") @DefaultValue("20") final int size
    ){
        PaginatedContent<WishlistItem> wishlistPage = bs.getWishlist(id, page, size);
        List<WishlistDTO> wishlist = wishlistPage.getPage().stream().map(WishlistDTO.mapper(uriInfo)).toList();
        return paginatedResponse(
                Response.ok(new GenericEntity<>(wishlist){}), wishlistPage, uriInfo
        ).build();
    }

    @GET
    @Path("{bookId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getWishlistItem(
            @PathParam("userId") final long userId,
            @PathParam("bookId") final long bookId
    ){
        Optional<WishlistItem> wishlistItem = bs.findWishlistItem(userId, bookId);
        if (wishlistItem.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(WishlistDTO.fromWishlistItem(uriInfo, wishlistItem.get())).build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response AddWishlistItem(
            @PathParam("userId") final long userId,
            WishlistCreateDTO wishlistDTO
    ){
        bs.addToWishlist(userId, wishlistDTO.getBookId());

        return Response
                .created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(wishlistDTO.getBookId())).build())
                .build();
    }

    @DELETE
    @Path("{bookId}")
    public Response RemoveWishlistItem(
        @PathParam("userId") final long userId,
        @PathParam("bookId") final long bookId
    ){
        bs.removeFromWishlist(userId, bookId);

        return Response.noContent().build();
    }
}

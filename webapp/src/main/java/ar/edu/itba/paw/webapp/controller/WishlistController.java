package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.WishlistService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.WishlistItem;
import ar.edu.itba.paw.models.exception.WishlistItemNotFoundException;
import ar.edu.itba.paw.webapp.contentType.VndMediaTypes;
import ar.edu.itba.paw.webapp.dto.input.WishlistCreateDTO;
import ar.edu.itba.paw.webapp.dto.output.WishlistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("users/{userId:\\d+}/wishlist")
@Component
public class WishlistController {

    private final WishlistService ws;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public WishlistController(final WishlistService ws){
        this.ws = ws;
    }

    @GET
    @Produces(value = {VndMediaTypes.WISHLIST})
    public Response getWishlist(
            @PathParam("userId") final long id,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("size") @DefaultValue("20") final int size
    ){
        PaginatedContent<WishlistItem> wishlistPage = ws.getWishlist(id, page, size);
        List<WishlistDTO> wishlist = wishlistPage.getPage().stream().map(WishlistDTO.mapper(uriInfo)).toList();
        return paginatedResponse(
                Response.ok(new GenericEntity<>(wishlist){}), wishlistPage, uriInfo
        ).build();
    }

    @GET
    @Path("{bookId:\\d+}")
    @Produces(value = {VndMediaTypes.WISHLIST})
    public Response getWishlistItem(
            @PathParam("userId") final long userId,
            @PathParam("bookId") final long bookId
    ){
        WishlistItem wishlistItem = ws.findWishlistItem(userId, bookId).orElseThrow(WishlistItemNotFoundException::new);
        return Response.ok(WishlistDTO.fromWishlistItem(uriInfo, wishlistItem)).build();
    }

    @POST
    @Consumes(value = {VndMediaTypes.WISHLIST})
    public Response AddWishlistItem(
            @PathParam("userId") final long userId,
            WishlistCreateDTO wishlistDTO
    ){
        ws.addToWishlist(userId, wishlistDTO.getBookId());

        return Response
                .created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(wishlistDTO.getBookId())).build())
                .build();
    }

    @DELETE
    @Path("{bookId:\\d+}")
    public Response RemoveWishlistItem(
        @PathParam("userId") final long userId,
        @PathParam("bookId") final long bookId
    ){
        ws.removeFromWishlist(userId, bookId);

        return Response.noContent().build();
    }
}

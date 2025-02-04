package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookGenreOrderBy;
import ar.edu.itba.paw.webapp.contentType.VndMediaTypes;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;


@Path("genres")
public class GenreController {
    private final GenreService gs;

    @Autowired
    public GenreController(GenreService gs){
        this.gs = gs;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {VndMediaTypes.GENRE})
    public Response getAllGenres(
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("size") @DefaultValue("20") final int size,
            @QueryParam("order_by") final BookGenreOrderBy orderBy
    ){
        final PaginatedContent<BookGenre> genrePage = gs.getGenres(orderBy, page, size);
        final List<String> genres = genrePage.getPage()
                .stream().map(BookGenre::toString).toList();

        return paginatedResponse(
                Response.ok(new GenericEntity<>(genres){}), genrePage, uriInfo
        ).build();
    }
}

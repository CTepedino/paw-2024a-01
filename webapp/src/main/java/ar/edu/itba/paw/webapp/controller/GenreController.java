package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookGenreOrderBy;
import ar.edu.itba.paw.webapp.contentType.VndMediaTypes;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;


@Path("genres")
public class GenreController {
    private final BookService bs;

    @Autowired
    public GenreController(BookService bs){
        this.bs = bs;
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
        final PaginatedContent<BookGenre> genrePage = bs.getGenres(orderBy, page, size);
        final List<String> genres = genrePage.getPage()
                .stream().map(BookGenre::toString).toList();

        return paginatedResponse(
                Response.ok(new GenericEntity<>(genres){}), genrePage, uriInfo
        ).build();
    }
}

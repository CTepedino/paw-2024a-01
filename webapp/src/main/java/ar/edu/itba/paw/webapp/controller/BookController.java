package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.webapp.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("books")
@Component
public class BookController {

    private static final int BOOKS_PAGE_SIZE = 20;

    @Autowired
    private BookService bs;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response listBooks(@QueryParam("page") @DefaultValue("1") final int page){
        final PaginatedContent<Book> booksPage = bs.getAll(page, BOOKS_PAGE_SIZE);
        final List<BookDTO> books = booksPage.getPage()
                .stream().map(BookDTO.mapper(uriInfo)).toList();

        return Response.ok(new GenericEntity<List<BookDTO>>(books) {})
                .link(URI.create("books?page=" + (page-1)), "prev")
                .link(URI.create("books?page=" + (page+1)), "next")
                .link(URI.create("books?page=1"), "first")
                .link(URI.create("books?page=" + booksPage.getPageNumber()), "last")
                .build();
    }
}

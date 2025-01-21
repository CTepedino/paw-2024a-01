package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.DealService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.files.BookFile;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import ar.edu.itba.paw.webapp.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.fileResponse;

@Path("books")
@Component
public class BookController {

    private static final int BOOKS_PAGE_SIZE = 20;

    private final BookService bs;
    private final DealService ds;

    @Autowired
    public BookController(BookService bs, DealService ds){
        this.bs = bs;
        this.ds = ds;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAllBooks(
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("size") @DefaultValue("20") final int pageSize,
            @QueryParam("title") final String title,
            @QueryParam("genre") final BookGenre genre,
            @QueryParam("min_price") final BigDecimal minPrice,
            @QueryParam("max_price") final BigDecimal maxPrice,
            @QueryParam("min_page_count") final Integer minPageCount,
            @QueryParam("max_page_count") final Integer maxPageCount,
            @QueryParam("min_suggested_age") final Integer minSuggestedAge,
            @QueryParam("max_suggested_age") final Integer maxSuggestedAge,
            @QueryParam("order_by") @DefaultValue("PUBLICATION_DATE_DESC") final BookSearchOrderBy orderBy
    ){
        final PaginatedContent<Book> booksPage = bs.searchWithParams(
                title,
                genre,
                minPrice, maxPrice,
                minPageCount, maxPageCount,
                minSuggestedAge, maxSuggestedAge,
                orderBy,
                page, pageSize
        );
        final List<BookDTO> books = booksPage.getPage()
                .stream().map(BookDTO.mapper(uriInfo)).toList();

        return ControllerUtils.paginatedResponse(
                Response.ok(new GenericEntity<>(books){}), booksPage, uriInfo
        ).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getBook(@PathParam("id") final long id){
        final BookDTO book = BookDTO.fromBook(uriInfo,
                bs.findById(id).orElseThrow(BookNotFoundException::new)
        );

        return Response.ok(book).build();
    }

    @GET
    @Path("/{id}/cover")
    @Produces(value = {"image/jpeg"})
    public Response getBookCover(@PathParam("id") final long id){
        CoverImage coverImage = bs.findById(id).orElseThrow(BookNotFoundException::new).getCoverImage();
        return fileResponse(coverImage).build();
    }

    @GET
    @Path("/{id}/preview")
    @Produces(value = {"application/pdf"})
    public Response getBookPreview(@PathParam("id") final long id){
        BookPreview preview = bs.findById(id).orElseThrow(BookNotFoundException::new).getPreview();
        return fileResponse(preview).build();
    }

    @GET
    @Path("/{id}/book_file")
    @Produces(value = {"application/pdf"})
    public Response getBookFile(@PathParam("id") final long id){
        BookFile bookFile = bs.findById(id).orElseThrow(BookNotFoundException::new).getBookFile();
        return fileResponse(bookFile).build();
    }
}

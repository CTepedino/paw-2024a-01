package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.DealService;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.files.BookFile;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.webapp.dto.BookDTO;
import ar.edu.itba.paw.webapp.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.fileResponse;
import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("books")
@Component
public class BookController {

    private final BookService bs;
    private final ReviewService rs;

    @Autowired
    public BookController(BookService bs, ReviewService rs){
        this.bs = bs;
        this.rs = rs;
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
            @QueryParam("order_by") @DefaultValue("PUBLICATION_DATE_DESC") final BookSearchOrderBy orderBy,
            @QueryParam("recommendations_for") final Long recommendationsForId
    ){
        final PaginatedContent<Book> booksPage = bs.listBooks(
                title,
                genre,
                minPrice, maxPrice,
                minPageCount, maxPageCount,
                minSuggestedAge, maxSuggestedAge,
                orderBy,
                page, pageSize,
                recommendationsForId
        );
        final List<BookDTO> books = booksPage.getPage()
                .stream().map(BookDTO.mapper(uriInfo)).toList();

        return paginatedResponse(
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

    @GET
    @Path("/{id}/reviews")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getReviews(
            @PathParam("id") final long id,
            @QueryParam("order_by") @DefaultValue("DATE_DESC") final ReviewOrderBy orderBy,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("size") @DefaultValue("20") final int size
    ){
        final PaginatedContent<Review> reviewPage =  rs.getAll(id, orderBy, page, size);
        final List<ReviewDTO> reviews = reviewPage.getPage()
                .stream().map(ReviewDTO.mapper(uriInfo)).toList();

        return paginatedResponse(
                Response.ok(new GenericEntity<>(reviews){}), reviewPage, uriInfo
        ).build();
    }

    @GET
    @Path("/{bookId}/reviews/{reviewerId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getReview(
            @PathParam("bookId") final long bookId,
            @PathParam("reviewerId") final long reviewerId
    ){
        Optional<Review> review = rs.find(bookId, reviewerId);
        if (review.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ReviewDTO.fromReview(uriInfo, review.get())).build();
    }
}

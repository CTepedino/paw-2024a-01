package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.DealService;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookAnalytics;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.deals.Deal;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.files.BookFile;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.UserAnalytics;
import ar.edu.itba.paw.webapp.contentType.VndMediaTypes;
import ar.edu.itba.paw.webapp.dto.input.BookCreateDTO;
import ar.edu.itba.paw.webapp.dto.input.BookEditDTO;
import ar.edu.itba.paw.webapp.dto.input.DealSubmitDTO;
import ar.edu.itba.paw.webapp.dto.input.validations.ImageFile;
import ar.edu.itba.paw.webapp.dto.input.validations.PdfFile;
import ar.edu.itba.paw.webapp.dto.output.*;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.fileResponse;
import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("books")
@Component
public class BookController {

    private final BookService bs;
    private final AnalyticsService as;
    private final DealService ds;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public BookController(final BookService bs, final AnalyticsService as, final DealService ds){
        this.bs = bs;
        this.as = as;
        this.ds = ds;
    }

    @GET
    @Produces(value = {VndMediaTypes.BOOK})
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
            @QueryParam("writer_id") final Long writerId,
            @QueryParam("owner_id") final Long ownerId,
            @QueryParam("recommendations_for_book") final Long recommendationsForId
    ){
        final PaginatedContent<Book> booksPage = bs.listBooks(
                title,
                genre,
                minPrice, maxPrice,
                minPageCount, maxPageCount,
                minSuggestedAge, maxSuggestedAge,
                orderBy,
                page, pageSize,
                writerId, ownerId,
                recommendationsForId
        );
        final List<BookDTO> books = booksPage.getPage()
                .stream().map(BookDTO.mapper(uriInfo)).toList();

        return paginatedResponse(
                Response.ok(new GenericEntity<>(books){}), booksPage, uriInfo
        ).build();
    }

    @POST
    @Consumes(value = {VndMediaTypes.BOOK})
    public Response addBook(@Valid BookCreateDTO bookDTO){

        long bookId = bs.create(
                bookDTO.getTitle(),
                bookDTO.getDescription(),
                bookDTO.getGenre(),
                bookDTO.getPrice(),
                bookDTO.getPageCount(),
                bookDTO.getSuggestedAge(),
                bookDTO.getPublicationDate(),
                bookDTO.getWriterId()
        );
        return Response
                .created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(bookId)).build())
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {VndMediaTypes.BOOK})
    public Response getBook(@PathParam("id") final long id){
        final BookDTO book = BookDTO.fromBook(uriInfo,
                bs.findById(id).orElseThrow(BookNotFoundException::new)
        );

        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(value = {VndMediaTypes.BOOK})
    public Response editBook(
            @PathParam("id") final long id,
            @Valid BookEditDTO bookDTO
    ){
        bs.editPublication(
                id,
                bookDTO.getTitle(),
                bookDTO.getDescription(),
                bookDTO.getGenre(),
                bookDTO.getPrice(),
                bookDTO.getPageCount(),
                bookDTO.getSuggestedAge()
        );

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/cover")
    @Produces(value = {"image/jpeg"})
    public Response getBookCover(@PathParam("id") final long id){
        CoverImage coverImage = bs.findById(id).orElseThrow(BookNotFoundException::new).getCoverImage();
        return fileResponse(coverImage, "image/jpeg").build();
    }

    @PUT
    @Path("/{id}/cover")
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response setBookCover(
            @PathParam("id") final long id,
            @ImageFile @FormDataParam("cover") FormDataBodyPart image
            ){
        bs.setCoverImage(id, image.getValueAs(byte[].class));
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/preview")
    @Produces(value = {"application/pdf"})
    public Response getBookPreview(@PathParam("id") final long id){
        BookPreview preview = bs.findById(id).orElseThrow(BookNotFoundException::new).getPreview();
        return fileResponse(preview, "application/pdf").build();
    }

    @PUT
    @Path("/{id}/preview")
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response setBookPreview(
            @PathParam("id") final long id,
            @PdfFile @FormDataParam("preview") FormDataBodyPart pdf
    ){
        bs.setPreview(id, pdf.getValueAs(byte[].class));
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/book_file")
    @Produces(value = {"application/pdf"})
    public Response getBookFile(@PathParam("id") final long id){
        BookFile bookFile = bs.findById(id).orElseThrow(BookNotFoundException::new).getBookFile();
        return fileResponse(bookFile, "application/pdf").build();
    }

    @PUT
    @Path("/{id}/book_file")
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response setBookFile(
            @PathParam("id") final long id,
            @PdfFile @FormDataParam("book_file") FormDataBodyPart pdf
    ){
        bs.setBookFile(id, pdf.getValueAs(byte[].class));
        return Response.noContent().build();
    }

    @GET
    @Path("{id}/deal")
    @Produces(value = {VndMediaTypes.DEAL})
    public Response getDeal(
            @PathParam("id") final long id
    ){
        Optional<Deal> deal = ds.get(id);
        if (deal.isPresent()){
            return Response.ok(DealDTO.fromDeal(uriInfo, deal.get())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}/deal")
    @Consumes(value = {VndMediaTypes.DEAL})
    public Response setDeal(
        @PathParam("id") final long id,
        @Valid DealSubmitDTO dealDTO
    ){
        ds.createOrUpdate(id, dealDTO.getPrice(), dealDTO.getDuration());
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}/deal")
    public Response endDeal(
            @PathParam("id") final long id
    ){
        ds.endDeal(id);
        return Response.noContent().build();
    }

    @GET
    @Path("{book_id}/monthly_analytics/{year_month:\\d{4}-\\d{2}}") //yyyy-MM
    @Produces(value = {VndMediaTypes.BOOK_ANALYTICS})
    public Response getMonthlyBookAnalytics(
            @PathParam("book_id") final long bookId,
            @PathParam("year_month") final String period
    ){
        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.parse(period);
        } catch (DateTimeParseException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        BookAnalytics analytics = as.getBookAnalytics(bookId, yearMonth);

        return Response.ok(BookMonthlyAnalyticsDTO.fromAnalytics(uriInfo, analytics)).build();
    }
}

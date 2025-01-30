package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.webapp.dto.output.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("books/{bookId}/reviews")
@Component
public class ReviewController {

    private final ReviewService rs;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public ReviewController(final ReviewService rs){
        this.rs = rs;
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getReviews(
            @PathParam("bookId") final long id,
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
    @Path("{reviewerId}")
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

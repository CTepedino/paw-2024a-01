package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Recommendation;
import ar.edu.itba.paw.webapp.dto.input.RecommendationCreateDTO;
import ar.edu.itba.paw.webapp.dto.output.RecommendationDTO;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("users/{userId}/recommendations")
@Component
public class RecommendationController {

    private final BookService bs;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RecommendationController(final BookService bs){
        this.bs = bs;
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRecommendations(
            @PathParam("userId") final long userId,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("size") @DefaultValue("20") final int size

    ){
        PaginatedContent<Recommendation> recommendationPage = bs.getRecommendations(userId, page, size);
        List<RecommendationDTO> recommendations = recommendationPage.getPage().stream().map(RecommendationDTO.mapper(uriInfo)).toList();
        return paginatedResponse(
                Response.ok(new GenericEntity<>(recommendations){}), recommendationPage, uriInfo
        ).build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response addRecommendation(
            @PathParam("userId") final long userId,
            RecommendationCreateDTO recommendationDTO
    ){
        bs.recommend(userId, recommendationDTO.getBookId());

        return Response
                .created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(recommendationDTO.getBookId())).build())
                .build();
    }

    @GET
    @Path("{bookId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRecommendation(
            @PathParam("userId") final long userId,
            @PathParam("bookId") final long bookId
    ){
        Optional<Recommendation> recommendation = bs.findRecommendation(userId, bookId);
        if(recommendation.isPresent()){
            return Response.ok(RecommendationDTO.fromRecommendation(uriInfo, recommendation.get())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{bookId}")
    public Response removeRecommendation(
            @PathParam("userId") final long userId,
            @PathParam("bookId") final long bookId
    ){
        bs.removeFromWishlist(userId, bookId);

        return Response.noContent().build();
    }

}

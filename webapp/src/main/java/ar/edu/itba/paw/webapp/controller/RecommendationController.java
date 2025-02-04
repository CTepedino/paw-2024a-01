package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.RecommendationsService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Recommendation;
import ar.edu.itba.paw.models.exception.RecommendationNotFoundException;
import ar.edu.itba.paw.webapp.contentType.VndMediaTypes;
import ar.edu.itba.paw.webapp.dto.input.RecommendationCreateDTO;
import ar.edu.itba.paw.webapp.dto.output.RecommendationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("users/{userId:\\d+}/recommendations")
@Component
public class RecommendationController {

    private final RecommendationsService rs;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RecommendationController(final RecommendationsService rs){
        this.rs = rs;
    }

    @GET
    @Produces(value = {VndMediaTypes.RECOMMENDATION})
    public Response getRecommendations(
            @PathParam("userId") final long userId,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("size") @DefaultValue("20") final int size

    ){
        PaginatedContent<Recommendation> recommendationPage = rs.getRecommendations(userId, page, size);
        List<RecommendationDTO> recommendations = recommendationPage.getPage().stream().map(RecommendationDTO.mapper(uriInfo)).toList();
        return paginatedResponse(
                Response.ok(new GenericEntity<>(recommendations){}), recommendationPage, uriInfo
        ).build();
    }

    @POST
    @Consumes(value = {VndMediaTypes.RECOMMENDATION})
    public Response addRecommendation(
            @PathParam("userId") final long userId,
            RecommendationCreateDTO recommendationDTO
    ){
        rs.recommend(userId, recommendationDTO.getBookId());

        return Response
                .created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(recommendationDTO.getBookId())).build())
                .build();
    }

    @GET
    @Path("{bookId:\\d+}")
    @Produces(value = {VndMediaTypes.RECOMMENDATION})
    public Response getRecommendation(
            @PathParam("userId") final long userId,
            @PathParam("bookId") final long bookId
    ){
        Recommendation recommendation = rs.findRecommendation(userId, bookId).orElseThrow(RecommendationNotFoundException::new);
        return Response.ok(RecommendationDTO.fromRecommendation(uriInfo, recommendation)).build();
    }

    @DELETE
    @Path("{bookId:\\d+}")
    public Response removeRecommendation(
            @PathParam("userId") final long userId,
            @PathParam("bookId") final long bookId
    ){
        rs.removeRecommendation(userId, bookId);

        return Response.noContent().build();
    }

}

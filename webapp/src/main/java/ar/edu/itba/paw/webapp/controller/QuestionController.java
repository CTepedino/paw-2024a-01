package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.QuestionService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.QuestionNotFoundException;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.webapp.dto.output.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.List;

import static ar.edu.itba.paw.webapp.controller.ControllerUtils.paginatedResponse;

@Path("questions")
@Component
public class QuestionController {
    private final QuestionService qs;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public QuestionController(final QuestionService qs){
        this.qs = qs;
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response listQuestions(
            @QueryParam("book_id") Long bookId,
            @QueryParam("writer_id") Long writerId,
            @QueryParam("questioner_id") Long questionerId,
            @QueryParam("exclude_questioner") @DefaultValue("false") boolean excludeQuestioner,
            @QueryParam("is_answered") Boolean isAnswered,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ){
        PaginatedContent<Question> questionPage = qs.searchQuestions(bookId, writerId, questionerId, excludeQuestioner, isAnswered, page, size);
        List<QuestionDTO> questions = questionPage.getPage().stream().map(QuestionDTO.mapper(uriInfo)).toList();

        return paginatedResponse(
                Response.ok(new GenericEntity<>(questions){}), questionPage, uriInfo
        ).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") final long id){
        Question question = qs.findById(id).orElseThrow(QuestionNotFoundException::new);

        return Response.ok(QuestionDTO.fromQuestion(uriInfo, question)).build();
    }
}

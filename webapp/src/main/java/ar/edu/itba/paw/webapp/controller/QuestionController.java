package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.QuestionService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.QuestionNotFoundException;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.webapp.contentType.VndMediaTypes;
import ar.edu.itba.paw.webapp.dto.input.AnswerSubmitDTO;
import ar.edu.itba.paw.webapp.dto.input.QuestionCreateDTO;
import ar.edu.itba.paw.webapp.dto.output.AnswerDTO;
import ar.edu.itba.paw.webapp.dto.output.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
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
    @Produces(value = {VndMediaTypes.QUESTION})
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

    @POST
    @Consumes(value = {VndMediaTypes.QUESTION})
    public Response makeQuestion(
            @Valid final QuestionCreateDTO questionDTO
    ){
        long questionId = qs.create(questionDTO.getBookId(), questionDTO.getQuestion());

        return Response
                .created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(questionId)).build())
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {VndMediaTypes.QUESTION})
    public Response getQuestion(@PathParam("id") final long id){
        Question question = qs.findById(id).orElseThrow(QuestionNotFoundException::new);

        return Response.ok(QuestionDTO.fromQuestion(uriInfo, question)).build();
    }

    @GET
    @Path("{id}/answer")
    @Produces(value = {VndMediaTypes.ANSWER})
    public Response getAnswer(@PathParam("id") final long id){
        Question question = qs.findById(id).orElseThrow(QuestionNotFoundException::new);
        if (question.getAnswer() == null){
            return Response.noContent().build();
        }

        return Response.ok(AnswerDTO.fromQuestion(uriInfo, question)).build();
    }

    @PUT
    @Path("/{id}/answer")
    @Consumes(value = {VndMediaTypes.ANSWER})
    public Response setAnswer(
            @PathParam("id") final long id,
            @Valid AnswerSubmitDTO answerDTO
    ){
        qs.answer(id, answerDTO.getAnswer());

        return Response.noContent().build();
    }
}

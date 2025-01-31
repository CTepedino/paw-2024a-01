package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.questions.Question;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

public class AnswerDTO {

    private long id;
    private long bookId;
    private long questionerId;
    private long writerId;
    private String answer;
    private LocalDateTime answerDate;

    private URI self;
    private URI question;

    public static AnswerDTO fromQuestion(UriInfo uriInfo, Question q){
        AnswerDTO dto = new AnswerDTO();

        dto.id = q.getQuestionId();
        dto.bookId = q.getBook().getBookId();
        dto.questionerId = q.getQuestioner().getUserId();
        dto.writerId = q.getBook().getWriter().getUserId();
        dto.answer = q.getAnswer();
        dto.answerDate = q.getAnswerDate();

        dto.self = uriInfo.getBaseUriBuilder().path("questions").path(String.valueOf(dto.id)).path("answer").build();
        dto.question = uriInfo.getBaseUriBuilder().path("questions").path(String.valueOf(dto.id)).build();

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getQuestionerId() {
        return questionerId;
    }

    public void setQuestionerId(long questionerId) {
        this.questionerId = questionerId;
    }

    public long getWriterId() {
        return writerId;
    }

    public void setWriterId(long writerId) {
        this.writerId = writerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(LocalDateTime answerDate) {
        this.answerDate = answerDate;
    }
}

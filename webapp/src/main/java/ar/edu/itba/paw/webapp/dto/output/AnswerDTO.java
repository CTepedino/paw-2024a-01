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
    private URI questioner;
    private URI writer;
    private URI book;

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
        dto.questioner = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.questionerId)).build();
        dto.writer = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.writerId)).build();
        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(dto.bookId)).build();
        return dto;
    }

    public URI getBook() {
        return book;
    }

    public void setBook(URI book) {
        this.book = book;
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

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getQuestion() {
        return question;
    }

    public void setQuestion(URI question) {
        this.question = question;
    }

    public URI getQuestioner() {
        return questioner;
    }

    public void setQuestioner(URI questioner) {
        this.questioner = questioner;
    }

    public URI getWriter() {
        return writer;
    }

    public void setWriter(URI writer) {
        this.writer = writer;
    }
}

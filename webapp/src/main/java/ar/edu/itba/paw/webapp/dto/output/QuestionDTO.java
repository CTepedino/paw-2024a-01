package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.questions.Question;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.function.Function;

public class QuestionDTO {

    private long id;
    private long bookId;
    private long questionerId;
    private long writerId;
    private String question;
    private LocalDateTime date;

    private URI self;
    private URI book;
    private URI questioner;
    private URI writer;
    private URI answer;

    public static Function<Question, QuestionDTO> mapper(UriInfo uriInfo){
        return q -> fromQuestion(uriInfo, q);
    }

    public static QuestionDTO fromQuestion(UriInfo uriInfo, Question q){
        QuestionDTO dto = new QuestionDTO();

        dto.id = q.getQuestionId();
        dto.bookId = q.getBook().getBookId();
        dto.questionerId = q.getQuestioner().getUserId();
        dto.writerId = q.getBook().getWriter().getUserId();
        dto.question = q.getQuestion();
        dto.date = q.getDate();

        dto.self = uriInfo.getBaseUriBuilder().path("questions").path(String.valueOf(dto.id)).build();
        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(dto.bookId)).build();
        dto.questioner = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.questionerId)).build();
        dto.writer = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.writerId)).build();
        if (q.getAnswer() != null){
            dto.answer = uriInfo.getBaseUriBuilder().path("questions").path(String.valueOf(dto.id)).path("answer").build();
        }
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getBook() {
        return book;
    }

    public void setBook(URI book) {
        this.book = book;
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

    public URI getAnswer() {
        return answer;
    }

    public void setAnswer(URI answer) {
        this.answer = answer;
    }
}

package ar.edu.itba.paw.webapp.dto.input;

import javax.validation.constraints.Size;

public class QuestionCreateDTO {

    @Size(min = 1, max = 500)
    private String question;

    private long bookId; 

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

}

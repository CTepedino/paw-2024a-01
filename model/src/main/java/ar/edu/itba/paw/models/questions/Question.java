package ar.edu.itba.paw.models.questions;

import ar.edu.itba.paw.models.users.User;

import java.time.LocalDate;

public class Question {

    private final long bookId;
    private final User questioner;
    private final String question;
    private final String answer;
    private final LocalDate date;

    private final LocalDate answerDate;

    public Question(long bookId, User questioner, String question, String answer, LocalDate date, LocalDate answerDate){
        this.bookId=bookId;
        this.questioner=questioner;
        this.question=question;
        this.answer=answer;
        this.date=date;
        this.answerDate=answerDate;
    }

    public long getBookId() {
        return bookId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public User getQuestioner() {
        return questioner;
    }

    public LocalDate getAnswerDate() {
        return answerDate;
    }
}

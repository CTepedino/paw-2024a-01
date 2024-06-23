package ar.edu.itba.paw.models.questions;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.users.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Entity
@Table(name="questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questions_question_id_seq")
    @SequenceGenerator(sequenceName = "questions_question_id_seq", name = "questions_question_id_seq", allocationSize = 1)
    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "questioner_id", referencedColumnName = "user_id", nullable = false)
    private User questioner;

    @Column
    private String question;

    @Column
    private String answer;

    @Column
    private LocalDateTime date;

    @Column(name = "answer_date")
    private LocalDateTime answerDate;

    Question(){}

    public Question(Book book, User questioner, String question, String answer, LocalDateTime date, LocalDateTime answerDate){
        this.book=book;
        this.questioner=questioner;
        this.question=question;
        this.answer=answer;
        this.date=date;
        this.answerDate=answerDate;
    }

    public long getQuestionId(){
        return questionId;
    }
    public Book getBook() {
        return book;
    }

    public LocalDateTime getDate() {
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

    public LocalDateTime getAnswerDate() {
        return answerDate;
    }

    public void setAnswer(String answer){
        this.answer=answer;
    }

    public void setAnswerDate(LocalDateTime answerDate){
        this.answerDate=answerDate;
    }

    public String getFormattedDate(Locale locale) {
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale));
    }

    public String getFormattedAnswerDate(Locale locale) {
        return answerDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale));
    }
}

package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.users.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuestionDao {
    Question create(Book book, User questioner, String questionText, LocalDateTime date);

    Optional<Question> findById(long id);

    void answer(Question question, String answer, LocalDateTime answerDate);

    List<Question> getAll(long bookId, int offset, int limit);

    long getAllSize(long bookId);

    List<Question> getAllFromUser(long userId, int offset, int limit);

    long getAllFromUserSize(long userId);

    List<Question> getAllFromUserAndBook(long userId, long bookId, int offset, int limit);

    public long getAllFromUserAndBookSize(long userId, long bookId);

    List<Question> getAllFromWriter(long userId, int offset, int limit);

    public long getAllFromWriterSize(long userId);

    List<Question> getAllFullQuestionsNotUser(long bookId, long userId, int offset, int limit);

    public long getAllFullQuestionsNotSUsersSize(long bookId, long userId);
}

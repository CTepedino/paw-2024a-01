package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.users.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuestionDao {

    List<Question> getAll(Long bookId, Long writerId, Long questionerId, boolean excludeQuestioner, Boolean isAnswered, int offset, int limit);
    long getAllSize(Long bookId, Long writerId, Long questionerId, boolean excludeQuestioner, Boolean isAnswered);

    Question create(Book book, User questioner, String questionText, LocalDateTime date);

    Optional<Question> findById(long id);

    void answer(Question question, String answer, LocalDateTime answerDate);

}

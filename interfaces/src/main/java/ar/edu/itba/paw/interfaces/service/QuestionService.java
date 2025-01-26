package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.users.User;

import java.util.Optional;

public interface QuestionService {

    PaginatedContent<Question> searchQuestions(Long bookId, Long writerId, Long questionerId, boolean excludeQuestioner, Boolean isAnswered, int pageNumber, int pageSize);

    void create(long bookId, String question);

    void answer(long questionId, String answer);

    PaginatedContent<Question> getAll(long bookId, int pageNumber, int pageSize, boolean isAuthor);

    PaginatedContent<Question> getAllFromUser(long userId, int pageNumber, int pageSize);

    PaginatedContent<Question> getAllFromUserAndBook(long userId, long bookId, int pageNumber, int pageSize);

    PaginatedContent<Question> getAllFromWriter(long userId, int pageNumber, int pageSize);

    PaginatedContent<Question> getAllFromWriter(long userId, int pageNumber, int pageSize, boolean viewComplete);

    PaginatedContent<Question> getAllFullQuestionsNotUser(long userId, long bookId, int pageNumber, int pageSize);

    long getQuestionCount(long bookId, User user, boolean includeUnanswered);

    long getMyQuestionCount(long userId, long bookId);

    boolean canAnswer(long questionId, String email);

    Optional<Question> findById(long questionId);
}

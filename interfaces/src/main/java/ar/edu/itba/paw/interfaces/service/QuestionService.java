package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.users.User;

import java.util.Optional;

public interface QuestionService {

    PaginatedContent<Question> searchQuestions(Long bookId, Long writerId, Long questionerId, boolean excludeQuestioner, Boolean isAnswered, int pageNumber, int pageSize);

    long create(long bookId, String question);

    void answer(long questionId, String answer);

    boolean canAnswer(long questionId, String email);

    Optional<Question> findById(long questionId);
}

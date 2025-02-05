package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.questions.Question;

import java.util.Optional;

public interface QuestionService {

    long create(long bookId, String question);

    void answer(long questionId, String answer);

    Optional<Question> findById(long questionId);

    PaginatedContent<Question> searchQuestions(Long bookId, Long writerId, Long questionerId, boolean excludeQuestioner, Boolean isAnswered, int pageNumber, int pageSize);

}

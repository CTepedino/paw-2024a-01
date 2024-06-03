package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.users.User;

public interface QuestionService {
    void create(long bookId, String question);

    void answer(long questionId, String answer);

    PaginatedContent<Question> getAll(long bookId, int pageNumber, int pageSize);
}

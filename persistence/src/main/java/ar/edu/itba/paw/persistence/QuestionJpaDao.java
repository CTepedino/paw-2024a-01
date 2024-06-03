package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.QuestionDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class QuestionJpaDao implements QuestionDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Question create(Book book, User questioner, String questionText, LocalDateTime date) {
        Question question = new Question(book, questioner, questionText, null, date, null);
        em.persist(question);
        return question;
    }

    @Override
    public Optional<Question> findById(long id) {
        return Optional.ofNullable(em.find(Question.class, id));
    }

    @Override
    public void answer(Question question, String answer, LocalDateTime answerDate) {
        question.setAnswer(answer);
        question.setAnswerDate(answerDate);
    }

    @Override
    public List<Question> getAll(long bookId, int offset, int limit) {
        TypedQuery<Question> query = em.createQuery(
                "FROM Question q WHERE q.book.bookId = :bookId ORDER BY q.date DESC",
                Question.class
        );
        query.setParameter("bookId", bookId);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public long getAllSize(long bookId) {
        return DaoUtils.getRowCount(em, "questions", "WHERE book_id = :bookId", Map.of("bookId", bookId));
    }
}

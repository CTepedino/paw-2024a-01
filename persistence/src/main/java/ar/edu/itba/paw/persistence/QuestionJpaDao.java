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
        Query nativeQuery = em.createNativeQuery("SELECT q.question_id FROM questions q WHERE q.book_id = :bookId ORDER BY q.date DESC");
        nativeQuery.setParameter("bookId", bookId);

        TypedQuery<Question> query = em.createQuery("FROM Question q WHERE q.questionId IN :idList ORDER BY q.date DESC", Question.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllSize(long bookId) {
        return DaoUtils.getRowCount(em, "Question q", "q.questionId", "WHERE q.book.bookId = :bookId", Map.of("bookId", bookId));
    }

    @Override
    public List<Question> getAllFromUser(long userId, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT q.question_id FROM questions q WHERE q.questioner_id = :userId ORDER BY q.date DESC");
        nativeQuery.setParameter("userId", userId);

        TypedQuery<Question> query = em.createQuery("FROM Question q WHERE q.questionId IN :idList ORDER BY q.date DESC", Question.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllFromUserSize(long userId) {
        return DaoUtils.getRowCount(em, "Question q", "q.questionId", "WHERE q.questioner.userId = :userId", Map.of("userId", userId));
    }

    @Override
    public List<Question> getAllFromUserAndBook(long userId, long bookId, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT q.question_id FROM questions q WHERE q.questioner_id = :userId AND q.book_id = :bookId ORDER BY q.date DESC");
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("bookId", bookId);

        TypedQuery<Question> query = em.createQuery("FROM Question q WHERE q.questionId IN :idList ORDER BY q.date DESC", Question.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllFromUserAndBookSize(long userId, long bookId) {
        return DaoUtils.getRowCount(em, "Question q", "q.questionId", "WHERE q.questioner.userId = :userId AND q.book.bookId = :bookId", Map.of("userId", userId, "bookId", bookId));
    }

    @Override
    public List<Question> getAllFromWriter(long userId, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT q.question_id FROM questions q JOIN books b ON q.book_id = b.book_id WHERE b.writer_id = :userId ORDER BY q.date DESC");
        nativeQuery.setParameter("userId", userId);

        TypedQuery<Question> query = em.createQuery("FROM Question q WHERE q.questionId IN :idList ORDER BY q.date DESC", Question.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllFromWriterSize(long userId) {
        return DaoUtils.getRowCount(em, "Question q LEFT JOIN Book b ON q.book.bookId = b.bookId", "q.questionId","WHERE b.writer.userId = :userId", Map.of("userId", userId));
    }

    @Override
    public List<Question> getAllFromWriterIncomplete(long userId, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT q.question_id FROM questions q JOIN books b ON q.book_id = b.book_id WHERE b.writer_id = :userId AND q.answer IS NULL ORDER BY q.date DESC");
        nativeQuery.setParameter("userId", userId);

        TypedQuery<Question> query = em.createQuery("FROM Question q WHERE q.questionId IN :idList ORDER BY q.date DESC", Question.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllFromWriterIncompleteSize(long userId) {
        return DaoUtils.getRowCount(em, "Question q LEFT JOIN Book b ON q.book.bookId = b.bookId", "q.questionId","WHERE b.writer.userId = :userId  AND q.answer IS NULL", Map.of("userId", userId));
    }

    @Override
    public List<Question> getAllFullQuestionsNotUser(long bookId, long userId, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT q.question_id FROM questions q WHERE q.questioner_id <> :userId AND q.book_id = :bookId AND q.answer IS NOT NULL ORDER BY q.date DESC");
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("bookId", bookId);

        TypedQuery<Question> query = em.createQuery("FROM Question q WHERE q.questionId IN :idList ORDER BY q.date DESC", Question.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllFullQuestionsNotUsersSize(long bookId, long userId) {
        return DaoUtils.getRowCount(em, "Question q", "q.questionId", "WHERE q.questioner.userId <> :userId AND q.book.bookId = :bookId and q.answer IS NOT NULL", Map.of("userId", userId, "bookId", bookId));
    }

    @Override
    public List<Question> getAllFullQuestions(long bookId, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT q.question_id FROM questions q WHERE q.book_id = :bookId AND q.answer IS NOT NULL ORDER BY q.date DESC");
        nativeQuery.setParameter("bookId", bookId);

        TypedQuery<Question> query = em.createQuery("FROM Question q WHERE q.questionId IN :idList ORDER BY q.date DESC", Question.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllFullQuestionsSize(long bookId) {
        return DaoUtils.getRowCount(em, "Question q", "q.questionId", "WHERE q.book.bookId = :bookId AND q.answer IS NOT NULL", Map.of("bookId", bookId));
    }
}

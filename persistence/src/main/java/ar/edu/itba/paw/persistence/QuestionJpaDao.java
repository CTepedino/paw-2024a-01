package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.QuestionDao;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
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
    public List<Question> getAll(Long bookId, Long writerId, Long questionerId, boolean excludeQuestioner, Boolean isAnswered, int offset, int limit) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT q.question_id FROM questions q JOIN books b ON q.book_id = b.book_id ");
        prepareSearchParams(nativeQueryStr, params, bookId, writerId, questionerId, excludeQuestioner, isAnswered);
        nativeQueryStr.append(" ORDER BY q.date DESC ");

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        params.forEach(nativeQuery::setParameter);

        TypedQuery<Question> query = em.createQuery(" FROM Question q WHERE q.questionId IN :idList ORDER BY q.date DESC ", Question.class);
        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllSize(Long bookId, Long writerId, Long questionerId, boolean excludeQuestioner, Boolean isAnswered) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT COUNT(DISTINCT q.question_id) FROM questions q LEFT JOIN books b ON q.book_id = b.book_id ");
        prepareSearchParams(nativeQueryStr, params, bookId, writerId, questionerId, excludeQuestioner, isAnswered);

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        params.forEach(nativeQuery::setParameter);

        return ((BigInteger) nativeQuery.getSingleResult()).longValue();
    }


    private void prepareSearchParams(StringBuilder query, Map<String, Object> params, Long bookId, Long writerId, Long questionerId, boolean excludeQuestioner, Boolean isAnswered){
        DaoUtils.addQueryCondition(query, " AND b.book_id = :bookId ", params, "bookId", bookId);
        DaoUtils.addQueryCondition(query, " AND b.writer_id = :writerId", params, "writerId", writerId);
        DaoUtils.addQueryCondition(query, " AND q.questioner_id " + (excludeQuestioner? "<>":"=") + " :questionerId ", params, "questionerId", questionerId);
        if (isAnswered != null){
            query.append(" AND q.answer IS ").append(isAnswered? "NOT NULL": "NULL");
        }
        int whereIndex = query.indexOf("AND");
        if (whereIndex != -1) {
            query.replace(whereIndex, whereIndex + 3, "WHERE");
        }
    }
}

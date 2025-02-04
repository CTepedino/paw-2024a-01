package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.RecommendationsDao;
import ar.edu.itba.paw.models.books.Recommendation;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RecommendationsJpaDao implements RecommendationsDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<Recommendation> getRecommendation(long userId, long bookId) {
        TypedQuery<Recommendation> query = em.createQuery("FROM Recommendation r WHERE r.userId = :userId AND r.bookId = :bookId", Recommendation.class);
        query.setParameter("userId", userId);
        query.setParameter("bookId", bookId);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void recommend(long userId, long bookId) {
        Recommendation recommendation = new Recommendation(userId, bookId);
        em.persist(recommendation);
    }

    @Override
    public void removeRecommendation(long userId, long bookId) {
        Query deleteQuery = em.createQuery("DELETE FROM Recommendation r WHERE r.userId = :userId AND r.bookId = :bookId");
        deleteQuery.setParameter("userId", userId);
        deleteQuery.setParameter("bookId", bookId);
        deleteQuery.executeUpdate();
    }

    @Override
    public List<Recommendation> getRecommendationsForBook(long userId, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT id FROM recommendations WHERE user_id = :userId");
        nativeQuery.setParameter("userId", userId);

        TypedQuery<Recommendation> query = em.createQuery("FROM Recommendation r WHERE r.id IN :idList", Recommendation.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getRecommendationsSize(long userId) {
        return DaoUtils.getRowCount(em, "Recommendation r", "r.id","WHERE r.userId = :userId", Map.of("userId", userId));
    }
}

package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReviewJpaDao implements ReviewDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(long bookId, User reviewer, int rating, String reviewText, LocalDateTime date) {
        Review review = new Review(bookId, reviewer, rating, reviewText, date);
        em.persist(review);
    }

    @Override
    public void modify(long bookId, User reviewer, int rating, String reviewText, LocalDateTime date) {
        get(bookId, reviewer).ifPresent(review -> {
            review.setRating(rating);
            review.setReview(reviewText);
            review.setDate(date);
            em.merge(review);
        });
    }

    @Override
    public List<Review> getAll(long bookId, ReviewOrderBy orderBy, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT review_id FROM reviews ORDER BY " + orderBy.getColumnName());
        nativeQuery.setMaxResults(limit);
        nativeQuery.setFirstResult(offset);

        @SuppressWarnings("unchecked")
        final List<Long> idList = (List<Long>) nativeQuery.getResultStream().map(n -> (Long)((Number)n).longValue()).collect(Collectors.toList());

        if (idList.isEmpty()){
            return Collections.emptyList();
        }

        TypedQuery<Review> query = em.createQuery("FROM Review r WHERE r.reviewId IN :idList ORDER BY " + orderBy.getColumnName(), Review.class);
        query.setParameter("idList", idList);
        return query.getResultList();
    }

    @Override
    public List<Review> getAllExcept(long bookId, ReviewOrderBy orderBy, int offset, int limit, long userId) {
        Query nativeQuery = em.createNativeQuery("SELECT review_id FROM reviews WHERE reviewer_id <> :userId ORDER BY " + orderBy.getColumnName());
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setMaxResults(limit);
        nativeQuery.setFirstResult(offset);

        @SuppressWarnings("unchecked")
        final List<Long> idList = (List<Long>) nativeQuery.getResultStream().map(n -> (Long)((Number)n).longValue()).collect(Collectors.toList());

        if (idList.isEmpty()){
            return Collections.emptyList();
        }

        TypedQuery<Review> query = em.createQuery("FROM Review r WHERE r.reviewId IN :idList ORDER BY " + orderBy.getColumnName(), Review.class);
        query.setParameter("idList", idList);
        return query.getResultList();
    }

    @Override
    public long getAllSize(long bookId) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM reviews WHERE book_id = :bookId");
        query.setParameter("bookId", bookId);
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    @Override
    public Optional<Review> get(long bookId, User reviewer) {
        TypedQuery<Review> query = em.createQuery("FROM Review r WHERE r.bookId = :bookId AND r.reviewer.userId = :userId", Review.class);
        query.setParameter("bookId", bookId);
        query.setParameter("userId", reviewer.getUserId());
        Review result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        return Optional.ofNullable(result);
    }

    @Override
    public int getAverageRating(long bookId) {
        TypedQuery<Double> query = em.createQuery("SELECT AVG(r.rating) FROM Review r WHERE r.bookId = :bookId", Double.class);
        query.setParameter("bookId", bookId);
        Double rating = query.getSingleResult();
        return (int) (rating==null?0:rating);
    }
}

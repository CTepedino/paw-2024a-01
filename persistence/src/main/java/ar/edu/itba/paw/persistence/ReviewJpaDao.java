package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ReviewJpaDao implements ReviewDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Review create(long bookId, User reviewer, int rating, String reviewText, LocalDateTime date) {
        Review review = new Review(bookId, reviewer, rating, reviewText, date);
        em.persist(review);
        return review;
    }

    @Override
    public void modify(Review review, int rating, String reviewText, LocalDateTime date) {
        review.setRating(rating);
        review.setReview(reviewText);
        review.setDate(date);
    }

    @Override
    public List<Review> getAll(long bookId, ReviewOrderBy orderBy, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT review_id FROM reviews WHERE book_id = :bookId ORDER BY " + orderBy.getColumnName());
        nativeQuery.setParameter("bookId", bookId);

        TypedQuery<Review> query = em.createQuery("FROM Review r WHERE r.reviewId IN :idList ORDER BY " + orderBy.getColumnName(), Review.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public List<Review> getAllExcept(long bookId, ReviewOrderBy orderBy, int offset, int limit, long userId) {
        Query nativeQuery = em.createNativeQuery("SELECT review_id FROM reviews WHERE reviewer_id <> :userId AND book_id = :bookId ORDER BY " + orderBy.getColumnName());
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("bookId", bookId);

        TypedQuery<Review> query = em.createQuery("FROM Review r WHERE r.reviewId IN :idList ORDER BY " + orderBy.getColumnName(), Review.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllSize(long bookId) {
        return DaoUtils.getRowCount(em, "reviews", "WHERE book_id = :bookId", Map.of("bookId", bookId));
    }

    @Override
    public Optional<Review> findById(long id) {
        return Optional.ofNullable(em.find(Review.class, id));
    }

    @Override
    public Optional<Review> find(long bookId, User reviewer) {
        TypedQuery<Review> query = em.createQuery("FROM Review r WHERE r.bookId = :bookId AND r.reviewer.userId = :userId", Review.class);
        query.setParameter("bookId", bookId);
        query.setParameter("userId", reviewer.getUserId());

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public int getAverageRating(long bookId) {
        TypedQuery<Double> query = em.createQuery("SELECT AVG(r.rating) FROM Review r WHERE r.bookId = :bookId", Double.class);
        query.setParameter("bookId", bookId);

        Double review = query.getSingleResult();
        return (int) (review!=null?Math.ceil(review):0);
    }
}

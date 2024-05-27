package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewKey;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        return List.of();
    }

    @Override
    public List<Review> getAllExcept(long bookId, ReviewOrderBy orderBy, int offset, int limit, long userId) {
        return List.of();
    }

    @Override
    public long getAllSize(long bookId) {
        return 0;
    }

    @Override
    public Optional<Review> get(long bookId, User reviewer) {
        return Optional.ofNullable(em.find(Review.class, new ReviewKey(bookId, reviewer)));
    }

    @Override
    public int getAverageRating(long bookId) {
        Query query = em.createQuery("SELECT AVG(r.rating) FROM Review r WHERE r.bookId = :bookId", Integer.class);
        query.setParameter("bookId", bookId);
        return (int) query.getSingleResult();
    }
}

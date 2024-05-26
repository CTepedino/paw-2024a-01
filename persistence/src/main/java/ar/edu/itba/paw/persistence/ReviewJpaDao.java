package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewJpaDao implements ReviewDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(long bookId, long reviewerId, int rating, String review) {

    }

    @Override
    public void modify(long bookId, long reviewerId, int rating, String review) {

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
    public Optional<Review> get(long bookId, long reviewerId) {
        return Optional.empty();
    }

    @Override
    public int getAverageRating(long bookId) {
        return 0;
    }
}

package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewDao {

    void create(long bookId, long reviewerId, int rating, String review, LocalDateTime time);

    void modify(long bookId, long reviewerId, int rating, String review);

    void delete(long bookId, long reviewerId);

    List<Review> getAll(long bookId, ReviewOrderBy orderBy, int offset, int limit);

}

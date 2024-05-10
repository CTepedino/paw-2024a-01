package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewDao {

    void create(long bookId, long reviewerId, int rating, String review);

    void modify(long bookId, long reviewerId, int rating, String review);


    List<Review> getAll(long bookId, ReviewOrderBy orderBy, int offset, int limit);
    long getAllSize(long bookId);

    Optional<Review> get(long bookId, long reviewerId);

    int getAverageRating(long bookId);

}

package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewDao {

    Review create(long bookId, User reviewer, int rating, String reviewText, LocalDateTime date);

    void modify(long bookId, User reviewer, int rating, String reviewText, LocalDateTime date);


    List<Review> getAll(long bookId, ReviewOrderBy orderBy, int offset, int limit);
    List<Review> getAllExcept(long bookId, ReviewOrderBy orderBy, int offset, int limit, long reviewerId);

    long getAllSize(long bookId);

    Optional<Review> find(long bookId, User reviewer);

    Optional<Review> findById(long id);

    int getAverageRating(long bookId);


}

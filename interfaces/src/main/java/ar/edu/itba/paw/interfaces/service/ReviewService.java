package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;

import java.util.Optional;

public interface ReviewService {

    void createOrUpdate(long bookId, long userId, int rating, String review);

    Optional<Review> get(long bookId, long userId);

    PaginatedContent<Review> getAll(long bookId, ReviewOrderBy orderBy, int pageNumber, int pageSize);

    int getAverageRating(long bookId);

    Optional<Review> findLoggedUserReview(long bookId);

}

package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReviewService {

    void createOrUpdate(long bookId, long reviewerId, int rating, String review);

    Optional<Review> find(long bookId, User user);
    Optional<Review> find(long bookId, long userId);

    PaginatedContent<Review> getAll(long bookId, ReviewOrderBy orderBy, int pageNumber, int pageSize);

    Optional<Review> findLoggedUserReview(long bookId);

}

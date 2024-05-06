package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;

public interface ReviewService {

    void create(long bookId, int rating, String review);

    void edit(long bookId, int rating, String review);

    void delete(long bookId);

    boolean hasReviewed(long bookId);

    PaginatedContent<Review> getAll(long bookId, ReviewOrderBy orderBy, int pageNumber, int pageSize);
}

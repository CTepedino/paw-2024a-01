package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;

    private final UserService us;

    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao, UserService us){
        this.reviewDao = reviewDao;
        this.us = us;
    }

    @Override
    public void create(long bookId, int rating, String review) {
        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        reviewDao.create(bookId, user.getUserId(), rating, review);
    }

    @Override
    public void edit(long bookId, int rating, String review) {
        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        reviewDao.modify(bookId, user.getUserId(), rating, review);
    }

    @Override
    public void delete(long bookId) {
        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        reviewDao.delete(bookId, user.getUserId());
    }

    @Override
    public boolean hasReviewed(long bookId) {
        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        return reviewDao.get(bookId, user.getUserId()).isPresent();
    }

    @Override
    public PaginatedContent<Review> getAll(long bookId, ReviewOrderBy orderBy, int pageNumber, int pageSize) {
        List<Review> reviews = reviewDao.getAll(bookId, orderBy, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(reviews, pageNumber, pageSize, reviewDao.getAllSize(bookId));
    }
}

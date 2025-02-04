package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.InvalidPageException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;

    private final UserService us;

    private final static Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao, UserService us){
        this.reviewDao = reviewDao;
        this.us = us;
    }

    @Transactional
    @Override
    public void createOrUpdate(long bookId, long reviewerId, int rating, String review){
        User user = us.findById(reviewerId).orElseThrow(UserNotFoundException::new);
        Optional<Review> maybeReview = reviewDao.find(bookId, reviewerId);
        if (maybeReview.isPresent()){
            reviewDao.modify(maybeReview.get(), rating, review, LocalDateTime.now());
            LOGGER.atDebug().setMessage("Modified Review for bookId: {}").addArgument(bookId).log();
        } else {
            reviewDao.create(bookId, user, rating, review, LocalDateTime.now());
            LOGGER.atDebug().setMessage("Created Review for bookId: {}").addArgument(bookId).log();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Review> find(long bookId, long userId) {
        return reviewDao.find(bookId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Review> getAll(long bookId, ReviewOrderBy orderBy, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }

        List<Review> reviews;
        long size = reviewDao.getAllSize(bookId);

        Optional<User> loggedUser = us.getLoggedUser();

        if (loggedUser.isPresent()){
            reviews = reviewDao.getAllExcept(bookId, orderBy, (pageNumber-1)*pageSize, pageSize, loggedUser.get().getUserId());
            Optional<Review> userReview = find(bookId, loggedUser.get().getUserId());
            if(userReview.isPresent()){
                size -=1;
            }
        } else {
            reviews = reviewDao.getAll(bookId, orderBy, (pageNumber-1)*pageSize, pageSize);
        }
        PaginatedContent<Review> page = new PaginatedContent<>(reviews, pageNumber, pageSize, size);
        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getAll(bookId, orderBy, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }

}

package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;

    private final UserService us;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao, UserService us){
        this.reviewDao = reviewDao;
        this.us = us;
    }

    @Transactional
    @Override
    public void createOrUpdate(long bookId, long userId, int rating, String review){
        if (get(bookId, userId).isPresent()){
            reviewDao.modify(bookId, userId, rating, review);
            LOGGER.atDebug().setMessage("Modified Review for bookId: {}").addArgument(bookId).log();
        } else {
            reviewDao.create(bookId, userId, rating, review);
            LOGGER.atDebug().setMessage("Created Review for bookId: {}").addArgument(bookId).log();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Review> get(long bookId, long userId) {
        return reviewDao.get(bookId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Review> getAll(long bookId, ReviewOrderBy orderBy, int pageNumber, int pageSize) {
        List<Review> reviews;
        long size = reviewDao.getAllSize(bookId);;

        if (us.isLoggedIn()){
            reviews = reviewDao.getAllExcept(bookId, orderBy, (pageNumber-1)*pageSize, pageSize, us.getLoggedUser().get().getUserId());
            size -=1;
        } else {
            reviews = reviewDao.getAll(bookId, orderBy, (pageNumber-1)*pageSize, pageSize);
        }
        return new PaginatedContent<>(reviews, pageNumber, pageSize, size);
    }

    @Transactional(readOnly = true)
    @Override
    public int getAverageRating(long bookId) {
        return reviewDao.getAverageRating(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Review> findLoggedUserReview(long bookId) {
        if(us.isLoggedIn()){
            return reviewDao.get(bookId, us.getLoggedUser().orElseThrow(UserNotFoundException::new).getUserId());
        }
        return Optional.empty();
    }

}

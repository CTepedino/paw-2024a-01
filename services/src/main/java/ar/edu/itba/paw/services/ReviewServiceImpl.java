package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.InvalidPageException;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public void createOrUpdate(long bookId, User user, int rating, String review){
        Optional<Review> maybeReview = reviewDao.find(bookId, user);
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
    public Optional<Review> find(long bookId, User user) {
        return reviewDao.find(bookId, user);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Review> getAll(long bookId, ReviewOrderBy orderBy, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }

        List<Review> reviews;
        long size = reviewDao.getAllSize(bookId);

        if (us.isLoggedIn()){
            reviews = reviewDao.getAllExcept(bookId, orderBy, (pageNumber-1)*pageSize, pageSize, us.getLoggedUser().get().getUserId());
            Optional<Review> userReview = findLoggedUserReview(bookId);
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

    @Transactional(readOnly = true)
    @Override
    public int getAverageRating(long bookId) {
        return reviewDao.getAverageRating(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Review> findLoggedUserReview(long bookId) {
        if(us.isLoggedIn()){
            return reviewDao.find(bookId, us.getLoggedUser().get());
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Override
    public HashMap<Long, Float> getBookRatings(List<Book> books){
        HashMap<Long, Float> ratings = new LinkedHashMap<>();
        books.forEach(book -> ratings.put(book.getBookId(), getAverageRating(book.getBookId())/2f));
        return ratings;
    }

}

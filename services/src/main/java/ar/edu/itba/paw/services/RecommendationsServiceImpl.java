package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.dao.RecommendationsDao;
import ar.edu.itba.paw.interfaces.service.RecommendationsService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.Recommendation;
import ar.edu.itba.paw.models.exception.AlreadyRecommendedException;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.InvalidRecommendationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationsServiceImpl implements RecommendationsService {

    private final RecommendationsDao recommendationsDao;
    private final OrderDao orderDao;
    private final BookDao bookDao;

    private final static Logger LOGGER = LoggerFactory.getLogger(RecommendationsServiceImpl.class);

    @Autowired
    public RecommendationsServiceImpl(final RecommendationsDao recommendationsDao, final OrderDao orderDao, final BookDao bookDao){
        this.recommendationsDao = recommendationsDao;
        this.orderDao = orderDao;
        this.bookDao = bookDao;
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Recommendation> getRecommendations(long userId, int page, int size) {
        List<Recommendation> recommendations = recommendationsDao.getRecommendationsForBook(userId, (page-1)*size, size);
        return new PaginatedContent<>(recommendations, page, size, recommendationsDao.getRecommendationsSize(userId));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Recommendation> findRecommendation(long userId, long bookId) {
        return recommendationsDao.getRecommendation(userId, bookId);
    }

    @Transactional
    @Override
    public void recommend(long userId, long bookId) {
        Book book = bookDao.findById(bookId).orElseThrow(BookNotFoundException::new);
        if (book.getWriter().getUserId() == userId || orderDao.find(userId, bookId).isEmpty()){
            throw new InvalidRecommendationException();
        }
        if (findRecommendation(userId, bookId).isPresent()) {
            throw new AlreadyRecommendedException();
        }
        recommendationsDao.recommend(userId, bookId);

        LOGGER.atDebug().setMessage("Added book {} to recommendations of user {}").addArgument(bookId).addArgument(userId).log();
    }

    @Transactional
    @Override
    public void removeRecommendation(long userId, long bookId) {
        recommendationsDao.removeRecommendation(userId, bookId);

        LOGGER.atDebug().setMessage("Removed book {} from recommendations of user {}").addArgument(bookId).addArgument(userId).log();
    }
}

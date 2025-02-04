package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.dao.WishlistDao;
import ar.edu.itba.paw.interfaces.service.WishlistService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.WishlistItem;
import ar.edu.itba.paw.models.exception.AlreadyWishlistedException;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.InvalidPageException;
import ar.edu.itba.paw.models.exception.InvalidWishlistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistDao wishlistDao;
    private final OrderDao orderDao;
    private final BookDao bookDao;

    private final static Logger LOGGER = LoggerFactory.getLogger(WishlistServiceImpl.class);

    @Autowired
    public WishlistServiceImpl(final WishlistDao wishlistDao, final OrderDao orderDao, final BookDao bookDao){
        this.wishlistDao = wishlistDao;
        this.orderDao = orderDao;
        this.bookDao = bookDao;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<WishlistItem> findWishlistItem(long userId, long bookId) {
        return wishlistDao.findWishlistItem(userId, bookId);
    }

    @Transactional
    @Override
    public void addToWishlist(long userId, long bookId) {
        Book book = bookDao.findById(bookId).orElseThrow(BookNotFoundException::new);
        if (book.getWriter().getUserId() == userId || orderDao.find(userId, bookId).isPresent()){
            throw new InvalidWishlistException();
        }
        if (wishlistDao.findWishlistItem(userId, bookId).isPresent()){
            throw new AlreadyWishlistedException();
        }
        wishlistDao.addToWishlist(userId, bookId);

        LOGGER.atDebug().setMessage("Added book {} to wishlist of user {}").addArgument(bookId).addArgument(userId).log();
    }

    @Transactional
    @Override
    public void removeFromWishlist(long userId, long bookId) {
        wishlistDao.removeFromWishlist(userId, bookId);
        LOGGER.atDebug().setMessage("Removed book {} from wishlist of user {}").addArgument(bookId).addArgument(userId).log();
    }

    @Transactional
    @Override
    public PaginatedContent<WishlistItem> getWishlist(long userId, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<WishlistItem> wishlist = wishlistDao.getWishlist(userId, (pageNumber-1)*pageSize, pageSize);

        PaginatedContent<WishlistItem> page = new PaginatedContent<>(wishlist, pageNumber, pageSize, wishlistDao.getWishlistSize(userId));
        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getWishlist(userId, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }

}

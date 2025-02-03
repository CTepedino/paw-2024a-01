package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.DealDao;
import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.DealService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.books.*;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.*;

import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final OrderDao orderDao;

    private final UserService us;

    private final static Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    public BookServiceImpl(final BookDao bookDao, final OrderDao orderDao, final UserService us){
        this.bookDao = bookDao;
        this.orderDao = orderDao;
        this.us = us;
    }

    @Transactional
    @Override
    public long create(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, LocalDate publishedDate, long writerId){
        User writer = us.findById(writerId).orElseThrow(UserNotFoundException::new);

        us.checkWriterRole(writer);
        Book book = bookDao.create(
                title,
                description,
                genre,
                price,
                pageCount,
                suggestedAge,
                publishedDate,
                writer,
                true
        );

        LOGGER.atDebug().setMessage("Created book: {}").addArgument(title).log();
        return book.getBookId();
    }

    @Transactional
    @Override
    public void editPublication(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge) {
        Book book = findById(bookId).orElseThrow(BookNotFoundException::new);

/*        if (cover != null*//* && !cover.isEmpty()*//*) {
            bookDao.updateCoverImage(book, cover);
        }
        if (preview != null*//* && !preview.isEmpty()*//*) {
            bookDao.updatePreviewFile(book, preview);
        }
        if (bookFile != null*//* && !bookFile.isEmpty()*//*) {
            bookDao.createOrUpdateBookFile(book, bookFile);
            if (pause){
                if (book.getWriter().getCbu()!=null){
                    pause = false;
                }
            }
        }*/
        bookDao.modify(book, title, description, genre, price, pageCount, suggestedAge);
        LOGGER.atDebug().setMessage("Publication for Book {} edited correctly").addArgument(title).log();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(long id) {
        return bookDao.findById(id);
    }


    @Transactional(readOnly = true)
    @Override
    public boolean isAuthor(Book book, long userId) {
        return book.getWriter().getUserId() == userId;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isAuthor(long userId, long bookId) {
        Optional<Book> maybeBook = bookDao.findById(bookId);
        return maybeBook.filter(b -> b.getWriter().getUserId() == userId).isPresent();
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<BookGenre> getGenres(BookGenreOrderBy orderBy, int pageNumber, int pageSize) {
        List<BookGenre> genres;

        if (orderBy == BookGenreOrderBy.BOOK_COUNT){
            genres = bookDao.getGenresByBookCount((pageNumber - 1) * pageSize, pageSize);
            List<BookGenre> booklessGenres = List.of(BookGenre.values());
            int i = 0;
            while (genres.size() < pageSize) {
                BookGenre genre = booklessGenres.get(i);
                if (!genres.contains(genre)) {
                    genres.add(genre);
                }
                i++;
            }
        } else {
            genres = List.of(BookGenre.values()).subList((pageNumber-1)*pageSize, pageNumber * pageSize);
        }

        return new PaginatedContent<>(genres, pageNumber, pageSize, BookGenre.values().length);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<WishlistItem> findWishlistItem(long userId, long bookId) {
        return bookDao.findWishlistItem(userId, bookId);
    }

    @Transactional
    @Override
    public void addToWishlist(long userId, long bookId) {
        Book book = bookDao.findById(bookId).orElseThrow(BookNotFoundException::new);
        if (book.getWriter().getUserId() == userId || orderDao.find(userId, bookId).isPresent()){
            throw new InvalidWishlistException();
        }
        if (bookDao.findWishlistItem(userId, bookId).isPresent()){
            throw new AlreadyWishlistedException();
        }
        bookDao.addToWishlist(userId, bookId);
    }

    @Transactional
    @Override
    public void removeFromWishlist(long userId, long bookId) {
        bookDao.removeFromWishlist(userId, bookId);
    }

    @Transactional
    @Override
    public PaginatedContent<WishlistItem> getWishlist(long userId, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<WishlistItem> wishlist = bookDao.getWishlist(userId, (pageNumber-1)*pageSize, pageSize);

        PaginatedContent<WishlistItem> page = new PaginatedContent<>(wishlist, pageNumber, pageSize, bookDao.getWishlistSize(userId));
        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getWishlist(userId, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }

    @Transactional
    @Override
    public void recheckWriterPausedBooks(long userId) {
        bookDao.recheckAllPaused(userId);
    }

    @Transactional
    @Override
    public void checkBookSalesCategory(Book book){
        long sales = orderDao.getTotalOrdersForBook(book.getBookId());
        if(sales >= BookSalesCategory.POPULAR.getMinSales() && book.getSalesCategory() == BookSalesCategory.DEFAULT){
            bookDao.updateSalesCategory(book, BookSalesCategory.POPULAR);
        }
        if(sales >= BookSalesCategory.BEST_SELLER.getMinSales() && book.getSalesCategory() == BookSalesCategory.POPULAR){
            bookDao.updateSalesCategory(book, BookSalesCategory.BEST_SELLER);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Book> listBooks(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int pageNumber, int pageSize, Long writerId, Long ownerId, Long recommendationsForId) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books;
        long size;
        if (recommendationsForId != null){
            Optional<Book> book = findById(recommendationsForId);
            if (book.isEmpty()){
                books = Collections.emptyList();
                size = 0;
            } else {
                books = bookDao.getRecommendations(book.get(), title,genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy, writerId, ownerId, (pageNumber-1)*pageSize, pageSize);
                size = bookDao.getRecommendationsSize(book.get(), title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, writerId, ownerId);
            }
        } else {
            switch(orderBy) {
                case BookSearchOrderBy.NEW_DEALS -> {
                    books = bookDao.getBooksWithNewDeals(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, writerId, ownerId,(pageNumber-1)*pageSize, pageSize);
                    size = bookDao.getBooksWithNewDealsSize(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, writerId, ownerId);
                }
                case BookSearchOrderBy.BEST_SELLERS -> {
                    books = bookDao.getTopBooks(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, writerId, ownerId,(pageNumber-1)*pageSize, pageSize);
                    size = bookDao.getTopBooksSize(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, writerId, ownerId);
                }
                default -> {
                    books = bookDao.searchWithParams(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy, writerId, ownerId, (pageNumber - 1) * pageSize, pageSize);
                    size = bookDao.getSearchSize(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, writerId, ownerId);
                }
            }
        }
        return new PaginatedContent<>(books, pageNumber, pageSize, size);
    }

    @Transactional
    @Override
    public void setCoverImage(long bookId, byte[] coverImage) {
        Book book = bookDao.findById(bookId).orElseThrow(BookNotFoundException::new);

        bookDao.createOrUpdateCoverImage(book, coverImage);
    }

    @Transactional
    @Override
    public void setPreview(long bookId, byte[] preview) {
        Book book = bookDao.findById(bookId).orElseThrow(BookNotFoundException::new);

        bookDao.createOrUpdatePreview(book, preview);
    }

    @Transactional
    @Override
    public void setBookFile(long bookId, byte[] file) {
        Book book = bookDao.findById(bookId).orElseThrow(BookNotFoundException::new);
        bookDao.unpause(book);

        bookDao.createOrUpdateBookFile(book, file);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Recommendation> getRecommendations(long userId, int page, int size) {
        List<Recommendation> recommendations = bookDao.getRecommendations(userId, (page-1)*size, size);
        return new PaginatedContent<>(recommendations, page, size, bookDao.getReccomendationsSize(userId));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Recommendation> findRecommendation(long userId, long bookId) {
        return bookDao.getRecommendation(userId, bookId);
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
        bookDao.recommend(userId, bookId);
    }

    @Transactional
    @Override
    public void removeRecommendation(long userId, long bookId) {
        bookDao.removeRecommendation(userId, bookId);
    }
}
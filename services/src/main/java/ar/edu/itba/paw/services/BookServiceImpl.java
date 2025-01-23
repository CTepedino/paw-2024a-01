package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.DealService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.books.*;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.*;

import ar.edu.itba.paw.models.users.User;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    public BookServiceImpl(final BookDao bookDao, final OrderDao orderDao){
        this.bookDao = bookDao;
        this.orderDao = orderDao;
    }

    @Transactional
    @Override
    public long create(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, LocalDate publishedDate, User writer, byte[] preview, byte[] cover, byte[] bookFile){

        Book book = bookDao.create(
                title,
                description,
                genre,
                price,
                pageCount,
                suggestedAge,
                publishedDate,
                writer,
                false
        );

        bookDao.createPreviewFile(book, preview);
        bookDao.createCoverImage(book, cover);
        bookDao.createOrUpdateBookFile(book, bookFile);

        LOGGER.atDebug().setMessage("Created book: {}").addArgument(title).log();
        return book.getBookId();
    }

    @Transactional
    @Override
    public void editPublication(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, byte[] cover, byte[] preview, byte[] bookFile) {
        Book book = findById(bookId).orElseThrow(BookNotFoundException::new);

        boolean pause = book.isPaused();

        if (cover != null/* && !cover.isEmpty()*/) {
            bookDao.updateCoverImage(book, cover);
        }
        if (preview != null/* && !preview.isEmpty()*/) {
            bookDao.updatePreviewFile(book, preview);
        }
        if (bookFile != null/* && !bookFile.isEmpty()*/) {
            bookDao.createOrUpdateBookFile(book, bookFile);
            if (pause){
                if (book.getWriter().getCbu()!=null){
                    pause = false;
                }
            }
        }
        bookDao.modify(book, title, description, genre, price, pageCount, suggestedAge, pause);
        LOGGER.atDebug().setMessage("Publication for Book {} edited correctly").addArgument(title).log();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(long id) {
        return bookDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Book> getWriterBooks(long writerId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books =  bookDao.getWriterBooks(writerId, title, orderBy, (pageNumber-1)*pageSize, pageSize);

        PaginatedContent<Book> page = new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getWriterBooksSize(writerId, title));
        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getWriterBooks(writerId, title, orderBy, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }


    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Book> getOwnedBooks(long readerId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize, boolean isPublic) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books = bookDao.getOwnedBooks(readerId, title, orderBy, (pageNumber-1)*pageSize, pageSize, isPublic);

        PaginatedContent<Book> page = new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getOwnedBooksSize(readerId, title, isPublic));
        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getOwnedBooks(readerId, title, orderBy, page.getPageCount(), pageSize, isPublic);
        } else {
            return page;
        }
    }


    @Transactional(readOnly = true)
    @Override
    public boolean isAuthor(Book book, long userId) {
        return book.getWriter().getUserId() == userId;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isAuthor(long bookId, String email) {
        Optional<Book> maybeBook = bookDao.findById(bookId);
        return maybeBook.isPresent() && maybeBook.get().getWriter().getEmail().equals(email);
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
    public PaginatedContent<Book> getProfileBooks(long userId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize, boolean asWriter , boolean ownsProfile) {
        if (asWriter){
            return getWriterBooks(userId, title, orderBy, pageNumber, pageSize);
        } else {
            return getOwnedBooks(userId, title, orderBy, pageNumber, pageSize, !ownsProfile);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<WishlistItem> findWishlistItem(long userId, long bookId) {
        return bookDao.findWishlistItem(userId, bookId);
    }

    @Transactional
    @Override
    public void toggleWishlist(long userId, long bookId) {
        if (findWishlistItem(userId, bookId).isPresent()){
            bookDao.removeFromWishlist(userId, bookId);
        } else {
            bookDao.addToWishlist(userId, bookId);
        }
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
    public PaginatedContent<Book> listBooks(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int pageNumber, int pageSize, Long recommendationsForId) {
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
                books = bookDao.getRecommendations(book.get(), title,genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy, (pageNumber-1)*pageSize, pageSize);
                size = bookDao.getRecommendationsSize(book.get(), title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
            }
        } else if (orderBy == BookSearchOrderBy.BEST_SELLERS){
            books = bookDao.getTopBooks(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge,(pageNumber-1)*pageSize, pageSize);
            size = bookDao.getTopBooksSize(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
        } else {
            books = bookDao.searchWithParams(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy, (pageNumber - 1) * pageSize, pageSize);
            size = bookDao.getSearchSize(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
        }
        return new PaginatedContent<>(books, pageNumber, pageSize, size);
    }
}
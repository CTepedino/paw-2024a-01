package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.models.books.AnalyticsBook;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.*;

import ar.edu.itba.paw.models.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    private final OrderDao orderDao;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public BookServiceImpl(final BookDao bookDao, final OrderDao orderDao){
        this.bookDao = bookDao;
        this.orderDao = orderDao;
    }

    @Transactional
    @Override
    public long create(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, User writer, MultipartFile preview, MultipartFile cover, MultipartFile bookFile){

        Book book = bookDao.create(
                title,
                description,
                genre,
                price,
                pageCount,
                suggestedAge,
                LocalDate.now(),
                writer,
                false
        );
        try {
            bookDao.createPreviewFile(book, preview.getBytes());
            bookDao.createCoverImage(book, cover.getBytes());
            bookDao.createOrUpdateBookFile(book, bookFile.getBytes());
        } catch (IOException e){
            LOGGER.atWarn().setMessage("Failed to create book: {} - Error Message: {}").addArgument(title).addArgument(e.getMessage()).log();
            throw new UnreadableFileException();
        }
        LOGGER.atDebug().setMessage("Created book: {}").addArgument(title).log();
        return book.getBookId();
    }

    @Transactional
    @Override
    public void editPublication(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, MultipartFile cover, MultipartFile preview, MultipartFile bookFile) {
        Book book = findById(bookId).orElseThrow(BookNotFoundException::new);

        boolean pause = book.isPaused();

        try {
            if (cover != null && !cover.isEmpty()) {
                bookDao.updateCoverImage(book, cover.getBytes());
            }
            if (preview != null && !preview.isEmpty()) {
                bookDao.updatePreviewFile(book, preview.getBytes());
            }
            if (bookFile != null && !bookFile.isEmpty()) {
                bookDao.createOrUpdateBookFile(book, bookFile.getBytes());
                if (pause){
                    if (book.getWriter().getCbu()!=null){
                        pause = false;
                    }
                }
            }

        } catch (IOException e){
            LOGGER.atWarn().setMessage("Failed to update book: {} - Error Message: {}").addArgument(title).addArgument(e.getMessage()).log();
            throw new UnreadableFileException();
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
    public PaginatedContent<Book> getAll(int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books = bookDao.getAll((pageNumber-1)*pageSize, pageSize);

        PaginatedContent<Book> page = new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getAllSize());
        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getAll(page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Book> searchWithParams(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books =  bookDao.searchWithParams(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy, (pageNumber-1)*pageSize, pageSize);

        PaginatedContent<Book> page = new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getSearchSize(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy));
        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return searchWithParams(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }


    @Transactional(readOnly = true)
    @Override
    public List<Book> getRecommendations(Book book){
        return bookDao.getRecommendations(book, 4);
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


    @Override
    public boolean isAuthor(Book book, long userId) {
        return book.getWriter().getUserId() == userId;
    }

    @Override
    public boolean isAuthor(long bookId, String email) {
        Optional<Book> maybeBook = bookDao.findById(bookId);
        return maybeBook.isPresent() && maybeBook.get().getWriter().getEmail().equals(email);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookGenre> getGenresByBookCount() {
        List<BookGenre> popularGenres = bookDao.getGenresByBookCount(12, 0);
        List<BookGenre> booklessGenres = List.of(BookGenre.values());
        int i = 0;
        while (popularGenres.size() < 12){
            BookGenre genre = booklessGenres.get(i);
            if (!popularGenres.contains(genre)){
                popularGenres.add(genre);
            }
            i++;
        }
        return popularGenres;
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
    public boolean isWishlisted(long userId, long bookId){
        return bookDao.findWishlistItem(userId, bookId).isPresent();
    }

    @Transactional
    @Override
    public void toggleWishlist(long userId, long bookId) {
        if (isWishlisted(userId, bookId)){
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
    public PaginatedContent<Book> getWishlist(long userId, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books = bookDao.getWishlist(userId, (pageNumber-1)*pageSize, pageSize);

        PaginatedContent<Book> page = new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getWishlistSize(userId));
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

    @Transactional(readOnly = true)
    @Override
    public List<Book> getTopBooks(){
        List<Long> books = orderDao.getTopBooks(6);
        return books.stream().map(book -> bookDao.findById(book).get()).toList();
    }

}

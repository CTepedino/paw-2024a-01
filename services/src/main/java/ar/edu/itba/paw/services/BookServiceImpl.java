package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.*;
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

    private final UserService us;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public BookServiceImpl(final BookDao bookDao, final UserService us){
        this.bookDao = bookDao;
        this.us = us;
    }

    @Transactional
    @Override
    public long create(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, long writerId, MultipartFile preview, MultipartFile cover, MultipartFile bookFile){

        Book book = bookDao.create(
                title,
                description,
                genre,
                price,
                pageCount,
                suggestedAge,
                LocalDate.now(),
                us.findById(writerId).get(),
                false
        );
        try {
            bookDao.createPreviewFile(book, preview.getBytes());
            bookDao.createCoverImage(book, cover.getBytes());
            bookDao.createBookFile(book, bookFile.getBytes());
        } catch (IOException e){
            LOGGER.atWarn().setMessage("Failed to create book: {} - Error Message: {}").addArgument(title).addArgument(e.getMessage()).log();
            throw new UnreadableFileException();
        }
        LOGGER.atDebug().setMessage("Created book: {}").addArgument(title).log();
        return book.getBookId();
    }

    @Transactional
    @Override
    public void editPublication(Book book, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, MultipartFile cover, MultipartFile preview, MultipartFile bookFile) {
        boolean isPaused = book.isPaused();

        try {
            if (cover != null && !cover.isEmpty()) {
                bookDao.updateCoverImage(book, cover.getBytes());
            }
            if (preview != null && !preview.isEmpty()) {
                bookDao.updatePreviewFile(book, preview.getBytes());
            }
            if (bookFile != null && !bookFile.isEmpty()) {
                bookDao.updateBookFile(book, bookFile.getBytes());
                if (isPaused){
                    isPaused = bookDao.recheckPaused(book.getBookId());
                }
            }

        } catch (IOException e){
            LOGGER.atWarn().setMessage("Failed to update book: {} - Error Message: {}").addArgument(title).addArgument(e.getMessage()).log();
            throw new UnreadableFileException();
        }
        bookDao.modify(book.getBookId(), title, description, genre, price, pageCount, suggestedAge, isPaused);
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
        return new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getAllSize());
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Book> searchWithParams(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books =  bookDao.searchWithParams(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getSearchSize(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy));
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
        return new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getWriterBooksSize(writerId, title));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Book> getOwnedBooks(long readerId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize, boolean isPublic) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books = bookDao.getOwnedBooks(readerId, title, orderBy, (pageNumber-1)*pageSize, pageSize, isPublic);
        return new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getOwnedBooksSize(readerId, title, isPublic));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean loggedUserIsAuthor(long bookId) {
        if (us.isLoggedIn()) {
            return bookDao.findById(bookId).orElseThrow(BookNotFoundException::new).getWriter().getEmail().equals(us.getLoggedUser().get().getEmail());
        }
        return false;
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
    public PaginatedContent<Book> getProfileBooks(long usedId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize, boolean asWriter , boolean ownsProfile) {
        if (asWriter){
            return getWriterBooks(usedId, title, orderBy, pageNumber, pageSize);
        } else {
            if(ownsProfile) {
                return getOwnedBooks(usedId, title, orderBy, pageNumber, pageSize, false);
            } else {
                return getOwnedBooks(usedId, title, orderBy, pageNumber, pageSize, true);
            }
        }
    }
}

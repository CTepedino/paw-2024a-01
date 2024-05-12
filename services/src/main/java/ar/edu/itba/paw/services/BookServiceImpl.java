package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.files.BookFileDao;
import ar.edu.itba.paw.interfaces.dao.files.BookPreviewDao;
import ar.edu.itba.paw.interfaces.dao.files.CoverImageDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.*;
import ar.edu.itba.paw.models.files.BookFile;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final BookPreviewDao previewDao;
    private final CoverImageDao coverDao;
    private final BookFileDao bookFileDao;

    private final UserService us;

    @Autowired
    public BookServiceImpl(final BookDao bookDao, final BookPreviewDao previewDao, final CoverImageDao coverDao, final BookFileDao bookFileDao, final UserService us){
        this.bookDao = bookDao;
        this.coverDao = coverDao;
        this.previewDao = previewDao;
        this.bookFileDao = bookFileDao;
        this.us = us;
    }

    @Transactional
    @Override
    public long create(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, long writerId, MultipartFile preview, MultipartFile cover, MultipartFile bookFile){
        try {
            long bookId = bookDao.create(
                    title,
                    description,
                    genre,
                    price,
                    pageCount,
                    suggestedAge,
                    writerId
            );
            previewDao.create(bookId, preview.getBytes());
            coverDao.create(bookId, cover.getBytes());
            bookFileDao.create(bookId, bookFile.getBytes());
            return bookId;
        } catch (IOException e){
            throw new UnreadableFileException();
        }
    }

    @Transactional
    @Override
    public void editPublication(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, boolean isPaused,MultipartFile cover, MultipartFile preview, MultipartFile bookFile) {
        try {
            if (!cover.isEmpty()) {
                coverDao.update(bookId, cover.getBytes());
            }
            if (!preview.isEmpty()) {
                previewDao.update(bookId, preview.getBytes());
            }
            if (!bookFile.isEmpty()) {
                bookFileDao.update(bookId, bookFile.getBytes());
                if (isPaused){
                    isPaused = bookDao.recheckPaused(bookId);
                }
            }

        } catch (IOException e){
            throw new UnreadableFileException();
        }
        bookDao.modify(bookId, title, description, genre, price, pageCount, suggestedAge, isPaused);
    }

    @Transactional(readOnly = true)
    @Override
    public CoverImage getCover(long id) {
        return coverDao.findById(id).orElseThrow(ImageNotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public BookPreview getPreview(long id) {
         return previewDao.findById(id).orElseThrow(PdfNotFoundException::new);
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
        return new PaginatedContent<Book>(books, pageNumber, pageSize, bookDao.getAllSize());
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Book> searchWithParams(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books =  bookDao.searchWithParams(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<Book>(books, pageNumber, pageSize, bookDao.getSearchSize(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy));
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
    public PaginatedContent<Book> getOwnedBooks(long readerId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books = bookDao.getOwnedBooks(readerId, title, orderBy, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getOwnedBooksSize(readerId, title));
    }


    @Transactional(readOnly = true)
    @Override
    public BookFile getBookFile(long bookId) {
        return bookFileDao.findById(bookId).orElseThrow(PdfNotFoundException::new);
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
}

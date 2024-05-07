package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.files.BookPreviewDao;
import ar.edu.itba.paw.interfaces.dao.files.CoverImageDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.ImageNotFoundException;
import ar.edu.itba.paw.models.exception.InvalidPageException;
import ar.edu.itba.paw.models.exception.PdfNotFoundException;
import ar.edu.itba.paw.models.exception.UnreadableFileException;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final BookPreviewDao previewDao;
    private final CoverImageDao coverDao;

    @Autowired
    public BookServiceImpl(final BookDao bookDao, final BookPreviewDao previewDao, final CoverImageDao coverDao){
        this.bookDao = bookDao;
        this.coverDao = coverDao;
        this.previewDao = previewDao;
    }

    @Transactional
    @Override
    public long create(String title, String description, BookGenre genre, double price, int pageCount, int suggestedAge, long writerId, MultipartFile preview, MultipartFile cover){
        try {
            long previewId = previewDao.create(preview.getBytes());
            long coverId = coverDao.create(cover.getBytes());
            return bookDao.create(
                    title,
                    description,
                    genre,
                    price,
                    pageCount,
                    suggestedAge,
                    writerId,
                    previewId,
                    coverId
            );
        } catch (IOException e){
            throw new UnreadableFileException();
        }
    }

    @Transactional
    @Override
    public void editPublication(long bookId, String title, String description, BookGenre genre, double price, int pageCount, int suggestedAge) {
        bookDao.modify(bookId, title, description, genre, price, pageCount, suggestedAge);
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
    public PaginatedContent<Book> searchWithParams(String title, BookGenre genre, Double minPrice, Double maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books =  bookDao.searchWithParams(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<Book>(books, pageNumber, pageSize, bookDao.getSearchSize(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAllGenre(BookGenre genre){
        List<Book> books = getAll(1, 20).getPage();
        List<Book> genreBooks = new ArrayList<>();

        for (Book book : books) {
            if (book.getGenre() == genre) {
                genreBooks.add(book);
            }
        }

        return genreBooks;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAllGenreExcluding(BookGenre genre, Book mainBook){
        List<Book> books = getAll(1, 20).getPage();
        List<Book> genreBooks = new ArrayList<>();

        for (Book book : books) {
            if (book.getGenre() == genre && book.getBookId() != mainBook.getBookId()) {
                genreBooks.add(book);
            }
        }

        return genreBooks;
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Book> getWriterBooks(long writerId, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Book> books = bookDao.getWriterBooks(writerId, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(books, pageNumber, pageSize, bookDao.getWriterBooksSize(writerId));
    }


}

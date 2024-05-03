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
            long coverId = coverDao.create(preview.getBytes());
            return bookDao.create(
                    title,
                    description,
                    genre,
                    price,
                    pageCount,
                    suggestedAge,
                    new Date(System.currentTimeMillis()),
                    writerId,
                    previewId,
                    coverId
            );
        } catch (IOException e){
            throw new UnreadableFileException();
        }

    }

    @Override
    public CoverImage getCover(long id) {
        return coverDao.findById(id).orElseThrow(ImageNotFoundException::new);
    }

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
        List<Book> books = bookDao.getAll((pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<Book>(books, pageNumber, pageSize, bookDao.getAllSize());
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Book> searchWithParams(String title, BookGenre genre, Double minPrice, Double maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int pageNumber, int pageSize) {
        List<Book> books =  bookDao.searchWithParams(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<Book>(books, pageNumber, pageSize, bookDao.getSearchSize(title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge, orderBy));
    }
}

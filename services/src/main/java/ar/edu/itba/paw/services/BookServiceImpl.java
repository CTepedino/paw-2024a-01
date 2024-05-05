package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.BookDao;
import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;
import ar.edu.itba.paw.models.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Autowired
    public BookServiceImpl(final BookDao bookDao){
        this.bookDao = bookDao;
    }

    @Transactional
    @Override
    public void create(
            String title,
            String description,
            BookGenre genre,
            double price,
            int pageCount,
            long pdfId,
            long imageId,
            int suggestedAge,
            long writerId
    ){
        bookDao.create(
                title,
                description,
                genre,
                price,
                pageCount,
                pdfId,
                imageId,
                suggestedAge,
                new Date(System.currentTimeMillis()),
                writerId
        );
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
}

package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.BookDao;
import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Autowired
    public BookServiceImpl(final BookDao bookDao){
        this.bookDao = bookDao;
    }

    @Override
    public Book create(
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

        return bookDao.create(
                title,
                description,
                genre.toString(),
                price,
                pageCount,
                pdfId,
                imageId,
                suggestedAge,
                new Date(System.currentTimeMillis()),
                writerId
        );
    }

    @Override
    public Optional<Book> findById(long id) {
        return bookDao.findById(id);
    }

    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }
}

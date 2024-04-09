package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.BookDao;
import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.models.Book;
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
            String genre,
            Double price,
            int pageCount,
            long pdfPreviewId,
            long imageId,
            int suggestedAge,
            Date publishDate,
            long writerId
    ){

        return bookDao.create(
                title,
                description,
                genre,
                price,
                pageCount,
                pdfPreviewId,
                imageId,
                suggestedAge,
                publishDate,
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

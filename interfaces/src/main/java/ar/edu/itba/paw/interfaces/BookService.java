package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


public interface BookService {
    void create(
            String title,
            String description,
            BookGenre genre,
            double price,
            int pageCount,
            long pdfId,
            long imageId,
            int suggestedAge,
            long writerId
    );

    Optional<Book> findById(long id);

    List<Book> getAll();
}

package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


public interface BookService {
    Book create(
            String title,
            String description,
            BookGenre genre,
            double price,
            int pageCount,
            long pdfId,
            long imageId,
            int suggestedAge,
            long writerId,

            String writerName,
            String writerLastName,
            String writerEmail
    );

    Optional<Book> findById(long id);

    List<Book> getAll();
}

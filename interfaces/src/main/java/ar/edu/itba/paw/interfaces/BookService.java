package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


public interface BookService {
    Book create(
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
    );

    Optional<Book> findById(long id);

    List<Book> getAll();
}

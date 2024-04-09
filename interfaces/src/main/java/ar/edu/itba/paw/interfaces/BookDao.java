package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Writer;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> findById(long id);
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

    List<Book> getAll();
}

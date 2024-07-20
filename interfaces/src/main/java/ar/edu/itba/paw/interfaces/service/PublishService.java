package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.users.User;
import java.math.BigDecimal;
import java.time.LocalDate;


public interface PublishService {

    long publishBook(
            User user,

            String cbu,
            String title,
            String description,
            BookGenre genre,
            int suggestedAge,
            BigDecimal price,
            int pageCount,
            LocalDate publicationDate,

            byte[] cover,
            byte[] preview,
            byte[] bookFile
    );
}

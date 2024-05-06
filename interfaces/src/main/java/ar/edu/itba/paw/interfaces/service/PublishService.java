package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.BookGenre;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;


public interface PublishService {

    long publishBook(

            String title,
            String description,
            BookGenre genre,
            int suggestedAge,
            BigDecimal price,
            int pageCount,

            MultipartFile cover,
            MultipartFile preview,
            MultipartFile bookFile
    );
}

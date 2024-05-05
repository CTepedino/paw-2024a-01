package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.BookGenre;
import org.springframework.web.multipart.MultipartFile;


public interface PublishService {

    long publishBook(

            String title,
            String description,
            BookGenre genre,
            int suggestedAge,
            double price,
            int pageCount,

            MultipartFile cover,
            MultipartFile preview
    );
}

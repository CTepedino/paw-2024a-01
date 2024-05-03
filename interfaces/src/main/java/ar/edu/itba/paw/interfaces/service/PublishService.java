package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;
import org.springframework.web.multipart.MultipartFile;


public interface PublishService {

    void publishBook(

            String title,
            String description,
            BookGenre genre,
            int suggestedAge,
            double price,
            int pageCount,

            MultipartFile image,
            MultipartFile pdf
    );
}

package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;
import org.springframework.web.multipart.MultipartFile;


public interface PublishService {

    Book publishBook(
            String writerFirstName,
            String writerLastName,
            String writerEmail,

            String title,
            String description,
            String genre,
            int suggestedAge,
            double price,
            int pageCount,

            MultipartFile image,
            MultipartFile pdf
    );
}

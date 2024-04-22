package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;
import org.springframework.web.multipart.MultipartFile;


public interface PublishService {

    Book publishBook(
            long writerId,

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

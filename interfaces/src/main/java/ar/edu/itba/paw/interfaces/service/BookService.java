package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;
import ar.edu.itba.paw.models.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import org.springframework.web.multipart.MultipartFile;

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
            int suggestedAge,
            long writerId,
            MultipartFile preview,
            MultipartFile cover
    );



    Optional<Book> findById(long id);


    PaginatedContent<Book> getAll(int pageNumber, int pageSize);



    PaginatedContent<Book> searchWithParams(
            String title,
            BookGenre genre,
            Double minPrice,
            Double maxPrice,
            Integer minPageCount,
            Integer maxPageCount,
            Integer minSuggestedAge,
            Integer maxSuggestedAge,
            BookSearchOrderBy orderBy,
            int pageNumber,
            int pageSize
    );

    CoverImage getCover(long id);

    BookPreview getPreview(long id);
}

package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookGenre;
import ar.edu.itba.paw.models.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;

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
            long pdfId,
            long imageId,
            int suggestedAge,
            long writerId
    );

    Optional<Book> findById(long id);

    PaginatedContent<Book> getAll(int pageNumber, int pageSize);

    public PaginatedContent<Book> searchWithParams(
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
}

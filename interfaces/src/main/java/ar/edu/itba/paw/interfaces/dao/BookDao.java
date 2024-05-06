package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;

import ar.edu.itba.paw.models.books.BookSearchOrderBy;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> findById(long id);
    long create(
            String title,
            String description,
            BookGenre genre,
            double price,
            int pageCount,
            int suggestedAge,

            long writerId,

            long previewId,
            long coverId
    );

    long modify(
            long bookId,

            String title,
            String description,
            BookGenre genre,
            double price,
            int pageCount,
            int suggestedAge
    );


    List<Book> getAll(int offset, int limit);
    int getAllSize();


    List<Book> searchWithParams(
            String title,
            BookGenre genre,
            Double minPrice,
            Double maxPrice,
            Integer minPageCount,
            Integer maxPageCount,
            Integer minSuggestedAge,
            Integer maxSuggestedAge,
            BookSearchOrderBy orderBy,
            int offset,
            int limit
    );
    int getSearchSize(
            String title,
            BookGenre genre,
            Double minPrice,
            Double maxPrice,
            Integer minPageCount,
            Integer maxPageCount,
            Integer minSuggestedAge,
            Integer maxSuggestedAge,
            BookSearchOrderBy orderBy
    );

    List<Book> getWriterBooks(
            long writerId,
            int offset,
            int limit
    );

    int getWriterBooksSize(long writerId);

}

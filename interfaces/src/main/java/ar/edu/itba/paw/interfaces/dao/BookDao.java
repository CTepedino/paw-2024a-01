package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;

import ar.edu.itba.paw.models.books.BookSearchOrderBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Book> findById(long id);
    long create(
            String title,
            String description,
            BookGenre genre,
            BigDecimal price,
            int pageCount,
            int suggestedAge,

            long writerId,

            long previewId,
            long coverId,
            long bookFileId
    );

    void modify(
            long bookId,

            String title,
            String description,
            BookGenre genre,
            BigDecimal price,
            int pageCount,
            int suggestedAge
    );


    List<Book> getAll(int offset, int limit);
    long getAllSize();


    List<Book> searchWithParams(
            String title,
            BookGenre genre,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minPageCount,
            Integer maxPageCount,
            Integer minSuggestedAge,
            Integer maxSuggestedAge,
            BookSearchOrderBy orderBy,
            int offset,
            int limit
    );
    long getSearchSize(
            String title,
            BookGenre genre,
            BigDecimal minPrice,
            BigDecimal maxPrice,
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

    long getWriterBooksSize(long writerId);

}

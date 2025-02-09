package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchQueryDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface BookService {
    long create(
            String title,
            String description,
            BookGenre genre,
            BigDecimal price,
            int pageCount,
            int suggestedAge,
            LocalDate publishedDate
    );

    void editPublication(
            long bookId,

            String title,
            String description,
            BookGenre genre,
            BigDecimal price,
            int pageCount,
            int suggestedAge,
            LocalDate publishedDate
    );

    void checkBookSalesCategory(Book book);

    void setCoverImage(long bookId, byte[] coverImage);
    void setPreview(long bookId, byte[] preview);
    void setBookFile(long bookId, byte[] file);


    Optional<Book> findById(long id);
    boolean isAuthor(long userId, long bookId);


    PaginatedContent<Book> listBooks(BookSearchQueryDTO queryDTO);

}

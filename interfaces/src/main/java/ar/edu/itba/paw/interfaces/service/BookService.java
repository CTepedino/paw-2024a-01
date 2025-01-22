package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.users.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface BookService {
    long create(
            String title,
            String description,
            BookGenre genre,
            BigDecimal price,
            int pageCount,
            int suggestedAge,
            LocalDate publishedDate,

            User writer,

            byte[] preview,
            byte[] cover,
            byte[] bookFile
    );

    void editPublication(
            long bookId,

            String title,
            String description,
            BookGenre genre,
            BigDecimal price,
            int pageCount,
            int suggestedAge,

            byte[] preview,
            byte[] cover,
            byte[] bookFile
    );


    Optional<Book> findById(long id);

    PaginatedContent<Book> getWriterBooks(long writerId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize);

    PaginatedContent<Book> getOwnedBooks(long readerId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize, boolean isPublic);

    PaginatedContent<Book> getProfileBooks(long userId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize, boolean asWriter, boolean ownsProfile);

    boolean isAuthor(Book book, long userId);
    boolean isAuthor(long bookId, String email);

    List<BookGenre> getGenresByBookCount();

    boolean isWishlisted(long userId, long bookId);

    void toggleWishlist(long userId, long bookId);

    void removeFromWishlist(long userId, long bookId);

    PaginatedContent<Book> getWishlist(long userId, int pageNumber, int pageSize);

    void recheckWriterPausedBooks(long userId);

    void checkBookSalesCategory(Book book);



    PaginatedContent<Book> listBooks(
        String title,
        BookGenre genre,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer minPageCount,
        Integer maxPageCount,
        Integer minSuggestedAge,
        Integer maxSuggestedAge,
        BookSearchOrderBy orderBy,
        int pageNumber,
        int pageSize,
        Long recommendationsForId
    );
}

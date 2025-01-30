package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.*;
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

            long writerId
    );

    void editPublication(
            long bookId,

            String title,
            String description,
            BookGenre genre,
            BigDecimal price,
            int pageCount,
            int suggestedAge
    );

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
            Long writerId,
            Long ownerId,
            Long recommendationsForId
    );

    Optional<Book> findById(long id);

    boolean isAuthor(Book book, long userId);
    boolean isAuthor(long bookId, String email);

    PaginatedContent<BookGenre> getGenres(BookGenreOrderBy orderBy, int pageNumber, int pageSize);

    Optional<WishlistItem> findWishlistItem(long userId, long bookId);
    void addToWishlist(long userId, long bookId);
    void removeFromWishlist(long userId, long bookId);
    PaginatedContent<WishlistItem> getWishlist(long userId, int pageNumber, int pageSize);

    void recheckWriterPausedBooks(long userId);

    void checkBookSalesCategory(Book book);

}

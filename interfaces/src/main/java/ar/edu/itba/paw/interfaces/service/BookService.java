package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.users.User;
import org.springframework.web.multipart.MultipartFile;

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

            MultipartFile preview,
            MultipartFile cover,
            MultipartFile bookFile
    );

    void editPublication(
            long bookId,

            String title,
            String description,
            BookGenre genre,
            BigDecimal price,
            int pageCount,
            int suggestedAge,

            MultipartFile preview,
            MultipartFile cover,
            MultipartFile bookFile
    );


    Optional<Book> findById(long id);

    PaginatedContent<Book> getAll(int pageNumber, int pageSize);


    PaginatedContent<Book> searchWithParams(
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
            int pageSize
    );


    List<Book> getRecommendations(Book book);


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

    List<Book> getTopBooks(Integer size);

    void recheckWriterPausedBooks(long userId);

    void checkBookSalesCategory(Book book);
}

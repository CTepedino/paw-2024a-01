package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;

import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.files.BookFile;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.users.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookDao {

    Optional<Book> findById(long id);

    Book create(
            String title,
            String description,
            BookGenre genre,
            BigDecimal price,
            int pageCount,
            int suggestedAge,
            LocalDate publishDate,
            User writer,
            boolean isPaused
    );

    void modify(
            Book book,

            String title,
            String description,
            BookGenre genre,
            BigDecimal price,
            int pageCount,
            int suggestedAge,
            boolean isPaused
    );

    CoverImage createCoverImage(Book book, byte[] coverImage);
    BookPreview createPreviewFile(Book book, byte[] previewFile);

    void updateCoverImage(Book book, byte[] coverImage);
    void updatePreviewFile(Book book, byte[] previewFile);

    BookFile createOrUpdateBookFile(Book book, byte[] bookFile);

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

    List<Book> getRecommendations(Book book, int max);

    List<Book> getWriterBooks(
            long writerId,
            String title,
            BookSearchOrderBy orderBy,
            int offset,
            int limit
    );

    long getWriterBooksSize(long writerId, String title);

    List<Book> getOwnedBooks(long readerId, String title, BookSearchOrderBy orderBy, int offset, int limit, boolean isPublic);

    long getOwnedBooksSize(long readerId, String title, boolean isPublic);

    List<BookGenre> getGenresByBookCount(int limit, int offset);

}

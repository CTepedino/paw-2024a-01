package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.books.*;

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
            int suggestedAge
    );

    void unpause(Book book);

    void updateSalesCategory(Book book, BookSalesCategory bookSalesCategory);

    void createOrUpdateCoverImage(Book book, byte[] coverImage);
    void createOrUpdatePreview(Book book, byte[] previewFile);
    void createOrUpdateBookFile(Book book, byte[] bookFile);

    List<Book> searchWithParams(BookSearchQueryDTO queryDTO);
    long getSearchSize(BookSearchQueryDTO queryDTO);

    List<Book> getRecommendationsForBook(BookSearchQueryDTO queryDTO);
    long getRecommendationsForBookSize(BookSearchQueryDTO queryDTO);

    List<Book> getTopBooks(BookSearchQueryDTO queryDTO);
    long getTopBooksSize(BookSearchQueryDTO queryDTO);

    List<Book> getBooksWithNewDeals(BookSearchQueryDTO queryDTO);
    long getBooksWithNewDealsSize(BookSearchQueryDTO queryDTO);

    List<BookGenre> getGenresByBookCount(int offset, int limit);


}

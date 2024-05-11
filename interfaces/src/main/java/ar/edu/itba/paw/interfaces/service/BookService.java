package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.files.BookFile;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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
            long writerId,
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

    CoverImage getCover(long id);

    BookPreview getPreview(long id);

    List<Book> getRecommendations(Book book);


    PaginatedContent<Book> getWriterBooks(long writerId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize);

    PaginatedContent<Book> getOwnedBooks(long readerId, String title, BookSearchOrderBy orderBy, int pageNumber, int pageSize);


    BookFile getBookFile(long bookId);

    boolean loggedUserIsAuthor(long bookId);
}

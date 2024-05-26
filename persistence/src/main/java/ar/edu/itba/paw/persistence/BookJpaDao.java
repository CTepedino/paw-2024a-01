package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class BookJpaDao implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.empty();
    }

    @Override
    public long create(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, long writerId) {
        return 0;
    }

    @Override
    public void modify(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, boolean isPaused) {

    }

    @Override
    public List<Book> getAll(int offset, int limit) {
        return List.of();
    }

    @Override
    public long getAllSize() {
        return 0;
    }

    @Override
    public List<Book> searchWithParams(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int offset, int limit) {
        return List.of();
    }

    @Override
    public long getSearchSize(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy) {
        return 0;
    }

    @Override
    public List<Book> getRecommendations(Book book, int max) {
        return List.of();
    }

    @Override
    public List<Book> getWriterBooks(long writerId, String title, BookSearchOrderBy orderBy, int offset, int limit) {
        return List.of();
    }

    @Override
    public long getWriterBooksSize(long writerId, String title) {
        return 0;
    }

    @Override
    public List<Book> getOwnedBooks(long readerId, String title, BookSearchOrderBy orderBy, int offset, int limit, boolean isPublic) {
        return List.of();
    }

    @Override
    public long getOwnedBooksSize(long readerId, String title, boolean isPublic) {
        return 0;
    }

    @Override
    public boolean recheckPaused(long bookId) {
        return false;
    }

    @Override
    public List<BookGenre> getGenresByBookCount(int limit, int offset) {
        return List.of();
    }
}

package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BookJpaDao implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public long create(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, LocalDate publishDate, User writer, boolean isPaused) {
        Book book = new Book(title, description, genre, price, pageCount, suggestedAge, publishDate, writer, isPaused);
        em.persist(book);
        return book.getBookId();
    }

    @Override
    public void modify(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, boolean isPaused) {
        Optional<Book> maybeBook = findById(bookId);
        if (maybeBook.isPresent()) {
            Book book = maybeBook.get();
            book.setTitle(title);
            book.setDescription(description);
            book.setGenre(genre);
            book.setPrice(price);
            book.setPageCount(pageCount);
            book.setSuggestedAge(suggestedAge);
            book.setPaused(isPaused);
            em.merge(book);
        }
    }

    @Override
    public List<Book> getAll(int offset, int limit) {
        TypedQuery<Book> query = em.createQuery("FROM Book WHERE isPaused = false", Book.class);

        return query.getResultList();
    }

    @Override
    public long getAllSize() {
        return 10;
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
        TypedQuery<BookGenre> query = em.createQuery(
         """
                SELECT b.genre
                FROM Book b
                GROUP BY b.genre
                ORDER BY COUNT(DISTINCT b.bookId) DESC
            """,
            BookGenre.class);
        return query.getResultList();
    }
}

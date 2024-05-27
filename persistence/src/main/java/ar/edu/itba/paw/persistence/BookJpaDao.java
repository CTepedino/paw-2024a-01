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
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @SuppressWarnings("unchecked")
    @Override
    public List<Book> getAll(int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT book_id FROM books WHERE is_paused = FALSE ORDER BY published_date DESC");
        nativeQuery.setMaxResults(limit);
        nativeQuery.setFirstResult(offset);

        final List<Long> idList = (List<Long>) nativeQuery.getResultList()
                .stream().map(n -> (Long)((Number)n).longValue()).collect(Collectors.toList());

        if (idList.isEmpty()){
            return Collections.emptyList();
        }

        final TypedQuery<Book> query = em.createQuery("FROM Book b WHERE b.bookId IN :idList ORDER BY b.publishDate DESC", Book.class);
        query.setParameter("idList", idList);
        return query.getResultList();
    }

    @Override
    public long getAllSize() {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM books WHERE is_paused = FALSE");
        return ((BigInteger) query.getSingleResult()).longValue();
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
        TypedQuery<Boolean> query = em.createQuery(
            """
                    SELECT NOT EXISTS (
                        SELECT 1
                        FROM Book b
                        JOIN User u ON b.writer.userId = u.userId
                        WHERE b.bookId = :bookId AND u.cbu IS NOT NULL AND EXISTS(
                            SELECT 1
                            FROM BookFile bf
                            WHERE bf.id = b.bookId
                        )
                    )
                """,
            Boolean.class
        );
        query.setParameter("bookId", bookId);
        return query.getSingleResult();
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
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();

        //TODO: preguntar si esta bien hacer esto asi
    }
}

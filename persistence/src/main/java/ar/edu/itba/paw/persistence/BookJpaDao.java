package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.books.WishlistItem;
import ar.edu.itba.paw.models.files.BookFile;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import ar.edu.itba.paw.models.users.User;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class BookJpaDao implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public Book create(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, LocalDate publishDate, User writer, boolean isPaused) {
        Book book = new Book(title, description, genre, price, pageCount, suggestedAge, publishDate, writer, isPaused);
        em.persist(book);
        return book;
    }

    @Override
    public void modify(Book book, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, boolean isPaused) {
        book.setTitle(title);
        book.setDescription(description);
        book.setGenre(genre);
        book.setPrice(price);
        book.setPageCount(pageCount);
        book.setSuggestedAge(suggestedAge);
        book.setPaused(isPaused);
    }

    @Override
    public CoverImage createCoverImage(Book book, byte[] coverImage) {
        CoverImage cover = new CoverImage(book.getBookId(), coverImage);
        em.persist(cover);
        return cover;
    }

    @Override
    public BookPreview createPreviewFile(Book book, byte[] previewFile) {
        BookPreview preview = new BookPreview(book.getBookId(), previewFile);
        em.persist(preview);
        return preview;
    }

    @Override
    public BookFile createBookFile(Book book, byte[] bookFile) {
        BookFile file = new BookFile(book.getBookId(), bookFile);
        em.persist(file);
        return file;
    }

    @Override
    public void updateCoverImage(Book book, byte[] coverImage) {
        book.getCoverImage().setFile(coverImage);
    }

    @Override
    public void updatePreviewFile(Book book, byte[] previewFile) {
        book.getPreview().setFile(previewFile);
    }

    @Override
    public void updateBookFile(Book book, byte[] bookFile) {
        book.getBookFile().setFile(bookFile);
    }

    @Override
    public List<Book> getAll(int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("SELECT book_id FROM books WHERE is_paused = FALSE ORDER BY published_date DESC");
        TypedQuery<Book> query = em.createQuery("FROM Book b WHERE b.bookId IN :idList ORDER BY b.publishDate DESC", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllSize() {
        return DaoUtils.getRowCount(em, "books WHERE is_paused = FALSE");
    }

    @Override
    public List<Book> searchWithParams(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int offset, int limit) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT b.book_id FROM books b ");
        prepareSearchQueryParams(nativeQueryStr, params, title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
        nativeQueryStr.append(" ORDER BY ").append(orderBy.getColumnName());

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        TypedQuery<Book> query = em.createQuery("FROM Book b WHERE b.bookId IN :idList ORDER BY " + orderBy.getColumnName(), Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getSearchSize(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        prepareSearchQueryParams(nativeQueryStr, params, title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);

        return DaoUtils.getRowCount(em, " books b ", nativeQueryStr.toString(), params);
    }

    private void prepareSearchQueryParams(StringBuilder nativeQueryStr, Map<String, Object> params, String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge){
        nativeQueryStr.append(" WHERE is_paused = FALSE AND LOWER(title) LIKE LOWER(:title) ");
        params.put("title", DaoUtils.prepareSearchString(title));
        if (genre != null) {
            DaoUtils.addQueryCondition(nativeQueryStr, " AND b.genre = :genre ", params, "genre", genre.toString());
        }
        DaoUtils.addQueryCondition(nativeQueryStr, " AND b.price >= :minPrice ", params, "minPrice", minPrice);
        DaoUtils.addQueryCondition(nativeQueryStr, " AND b.price <= :maxPrice ", params, "maxPrice", maxPrice);
        DaoUtils.addQueryCondition(nativeQueryStr, " AND b.page_count >= :minPageCount", params, "minPageCount", minPageCount);
        DaoUtils.addQueryCondition(nativeQueryStr, " AND b.page_count <= :maxPageCount ", params, "maxPageCount", maxPageCount);
        DaoUtils.addQueryCondition(nativeQueryStr, " AND b.suggested_age >= :minSuggestedAge ", params, "minSuggestedAge", minSuggestedAge);
        DaoUtils.addQueryCondition(nativeQueryStr, " AND b.suggested_age <= :maxSuggestedAge ", params, "maxSuggestedAge", maxSuggestedAge);
    }

    @Override
    public List<Book> getRecommendations(Book book, int max) {
        Query nativeQuery = em.createNativeQuery(
     """
            SELECT b.book_id
            FROM books b JOIN users u ON b.writer_id = u.user_id LEFT JOIN orders o ON b.book_id = o.book_id AND o.status = 'COMPLETED'
            WHERE b.is_paused = FALSE AND (b.genre = :genre OR b.writer_id = :writerId) AND b.book_id <> :bookId
            GROUP BY b.book_id, u.user_id
            ORDER BY COUNT(o.book_id) DESC
        """);
        nativeQuery.setParameter("genre", book.getGenre().toString());
        nativeQuery.setParameter("writerId", book.getWriter().getUserId());
        nativeQuery.setParameter("bookId", book.getBookId());

        TypedQuery<Book> query = em.createQuery("FROM Book b WHERE b.bookId IN :idList", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, 0, max);
    }

    @Override
    public List<Book> getWriterBooks(long writerId, String title, BookSearchOrderBy orderBy, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT book_id
            FROM books
            WHERE writer_id = :writerId AND LOWER(title) LIKE LOWER(:title)
            ORDER BY \s
       """ + orderBy.getColumnName());
        nativeQuery.setParameter("writerId", writerId);
        nativeQuery.setParameter("title", DaoUtils.prepareSearchString(title));

        TypedQuery<Book> query = em.createQuery("FROM Book b WHERE b.bookId IN :idList ORDER BY " + orderBy.getColumnName(), Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getWriterBooksSize(long writerId, String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("writerId", writerId);
        params.put("title", DaoUtils.prepareSearchString(title));

        return DaoUtils.getRowCount(em, "books", " WHERE writer_id = :writerId AND LOWER(title) LIKE LOWER(:title) ", params);
    }

    @Override
    public List<Book> getOwnedBooks(long readerId, String title, BookSearchOrderBy orderBy, int offset, int limit, boolean isPublic) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT b.book_id
            FROM books b JOIN orders o ON b.book_id = o.book_id
            WHERE LOWER(b.title) LIKE LOWER(:title) AND o.status = 'COMPLETED' AND o.buyer_id = :readerId
        """ + (isPublic?" AND o.is_public = TRUE ":"") + " ORDER BY " + orderBy.getColumnName());
        nativeQuery.setParameter("readerId", readerId);
        nativeQuery.setParameter("title", DaoUtils.prepareSearchString(title));

        TypedQuery<Book> query = em.createQuery("FROM Book b WHERE b.bookId IN :idList ORDER BY " + orderBy.getColumnName(), Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getOwnedBooksSize(long readerId, String title, boolean isPublic) {
        Map<String, Object> params = new HashMap<>();
        params.put("readerId", readerId);
        params.put("title", DaoUtils.prepareSearchString(title));

        return DaoUtils.getRowCount(em,
                "books b LEFT JOIN orders o ON b.book_id = o.book_id",
                "WHERE LOWER(b.title) LIKE LOWER(:title) AND o.status = 'COMPLETED' AND o.buyer_id = :readerId" + (isPublic?" AND o.is_public = TRUE ":""),
                params
        );
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

    @SuppressWarnings("unchecked")
    @Override
    public List<BookGenre> getGenresByBookCount(int limit, int offset) {
        Query query = em.createNativeQuery("SELECT genre FROM books GROUP BY genre ORDER BY COUNT(*) DESC");
        query.setMaxResults(limit);
        query.setFirstResult(offset);

        return (List<BookGenre>) query.getResultStream().map(genre -> BookGenre.valueOf((String) genre)).collect(Collectors.toList());
    }


    @Override
    public Optional<WishlistItem> findWishlistItem(long userId, long bookId) {
        TypedQuery<WishlistItem> query = em.createQuery("FROM WishlistItem w WHERE w.userId = :userId AND w.bookId = :bookId", WishlistItem.class);
        query.setParameter("userId", userId);
        query.setParameter("bookId", bookId);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public WishlistItem addToWishlist(long userId, long bookId){
        WishlistItem wishlistItem = new WishlistItem(userId, bookId);
        em.persist(wishlistItem);
        return wishlistItem;
    }

    @Override
    public void removeFromWishlist(long userId, long bookId){
        Query deleteQuery = em.createQuery("DELETE FROM WishlistItem w WHERE w.userId = :userId AND w.bookId = :bookId");
        deleteQuery.setParameter("userId", userId);
        deleteQuery.setParameter("bookId", bookId);
        deleteQuery.executeUpdate();
    }

    @Override
    public List<Book> getWishlist(long userId, int offset, int limit){
        Query nativeQuery = em.createNativeQuery("SELECT book_id FROM wishlist WHERE user_id = :userId");
        nativeQuery.setParameter("userId", userId);

        TypedQuery<Book> query = em.createQuery("FROM Book b WHERE b.bookId IN :idList", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getWishlistSize(long userId) {
        return DaoUtils.getRowCount(em, "wishlist", "WHERE user_id = :userId", Map.of("userId", userId));
    }
}

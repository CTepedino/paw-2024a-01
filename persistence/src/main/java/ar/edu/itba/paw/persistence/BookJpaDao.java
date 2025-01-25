package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.*;
import ar.edu.itba.paw.models.files.BookFile;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
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
    public void updateSalesCategory(Book book, BookSalesCategory bookSalesCategory){
        book.setSalesCategory(bookSalesCategory);
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

    private BookFile createBookFile(Book book, byte[] bookFile) {
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

    private void updateBookFile(Book book, byte[] bookFile) {
        book.getBookFile().setFile(bookFile);
    }

    @Override
    public BookFile createOrUpdateBookFile(Book book, byte[] bookFile) {
        if (book.getBookFile()==null){
            createBookFile(book, bookFile);
        } else {
            updateBookFile(book, bookFile);
        }
        return book.getBookFile();
    }

    private void prepareSearchQueryParams(StringBuilder nativeQueryStr, Map<String, Object> params, String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge){
        prepareSearchQueryParams(nativeQueryStr, params, title, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
        if (genre != null) {
            DaoUtils.addQueryCondition(nativeQueryStr, " AND b.genre = :genre ", params, "genre", genre.toString());
        }
    }

    private void prepareSearchQueryParams(StringBuilder nativeQueryStr, Map<String, Object> params, String title, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge){
        nativeQueryStr.append(" WHERE b.is_paused = FALSE AND LOWER(title) LIKE LOWER(:title) ");
        params.put("title", DaoUtils.prepareSearchString(title));
        DaoUtils.addQueryCondition(nativeQueryStr, " AND ((d.price IS NULL AND b.price >= :minPrice) OR d.price >= :minPrice ) ", params, "minPrice", minPrice);
        DaoUtils.addQueryCondition(nativeQueryStr, " AND ((d.price IS NULL AND b.price <= :maxPrice) OR d.price <= :maxPrice ) ", params, "maxPrice", maxPrice);
        DaoUtils.addQueryCondition(nativeQueryStr, " AND b.page_count >= :minPageCount ", params, "minPageCount", minPageCount);
        DaoUtils.addQueryCondition(nativeQueryStr, " AND b.page_count <= :maxPageCount ", params, "maxPageCount", maxPageCount);
        DaoUtils.addQueryCondition(nativeQueryStr, " AND b.suggested_age >= :minSuggestedAge ", params, "minSuggestedAge", minSuggestedAge);
        DaoUtils.addQueryCondition(nativeQueryStr, " AND b.suggested_age <= :maxSuggestedAge ", params, "maxSuggestedAge", maxSuggestedAge);
    }

    @Override
    public List<Book> searchWithParams(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int offset, int limit) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT b.book_id FROM books b LEFT JOIN deals d ON b.book_id = d.id ");
        prepareSearchQueryParams(nativeQueryStr, params, title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
        nativeQueryStr.append(" ORDER BY ").append(orderBy.getColumnName());

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }

       TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b LEFT JOIN b.deal d WHERE b.bookId IN :idList ORDER BY " + orderBy.getColumnName(), Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getSearchSize(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        prepareSearchQueryParams(nativeQueryStr, params, title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);

        Query query = em.createNativeQuery("SELECT COUNT(DISTINCT b.book_id) FROM books b LEFT JOIN deals d ON b.book_id = d.id " + nativeQueryStr);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    @Override
    public List<Book> getRecommendations(Book book, String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, BookSearchOrderBy orderBy, int offset, int limit) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (orderBy == null){
            orderBy = BookSearchOrderBy.BEST_SELLERS;
        }

        nativeQueryStr.append("SELECT b.book_id FROM books b LEFT JOIN deals d ON b.book_id = d.id JOIN users u ON b.writer_id = u.user_id LEFT JOIN orders o ON b.book_id = o.book_id AND o.status = 'COMPLETED' ");
        prepareSearchQueryParams(nativeQueryStr, params, title, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
        nativeQueryStr.append(" AND (b.genre = :genre OR b.writer_id = :writerId) AND b.book_id <> :bookId GROUP BY b.book_id, u.user_id ");
        nativeQueryStr.append(" ORDER BY ").append(orderBy.getColumnName());

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }
        nativeQuery.setParameter("genre", genre == null? book.getGenre().toString() : genre.toString());
        nativeQuery.setParameter("writerId", book.getWriter().getUserId());
        nativeQuery.setParameter("bookId", book.getBookId());

        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b LEFT JOIN b.deal d WHERE b.bookId IN :idList", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getRecommendationsSize(Book book, String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT COUNT(DISTINCT b.book_id) FROM books b LEFT JOIN deals d ON b.book_id = d.id JOIN users u ON b.writer_id = u.user_id LEFT JOIN orders o ON b.book_id = o.book_id AND o.status = 'COMPLETED' ");
        prepareSearchQueryParams(nativeQueryStr, params, title, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
        nativeQueryStr.append(" AND (b.genre = :genre OR b.writer_id = :writerId) AND b.book_id <> :bookId");

        Query query = em.createNativeQuery(nativeQueryStr.toString());
        query.setParameter("genre", genre == null? book.getGenre().toString() : genre.toString());
        query.setParameter("writerId", book.getWriter().getUserId());
        query.setParameter("bookId", book.getBookId());

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return ((BigInteger) query.getSingleResult()).longValue();
    }


    @Override
    public List<Book> getTopBooks(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, int offset, int limit) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT o.book_id FROM orders o LEFT JOIN books b ON o.book_id = b.book_id ");
        prepareSearchQueryParams(nativeQueryStr, params, title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
        nativeQueryStr.append(" GROUP BY o.book_id ");

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b JOIN Order o ON o.book.bookId = b.bookId WHERE b.bookId IN :idList GROUP BY b, o.book.bookId ORDER BY COUNT(o.book.bookId) DESC", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getTopBooksSize(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT COUNT(DISTINCT o.book_id) FROM orders o LEFT JOIN books b ON o.book_id = b.book_id ");
        prepareSearchQueryParams(nativeQueryStr, params, title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        return ((BigInteger) nativeQuery.getSingleResult()).longValue();
    }


    @Override
    public List<Book> getBooksWithNewDeals(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, int offset, int limit) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append(" SELECT d.id FROM deals d LEFT JOIN books b ON b.book_id = d.id ");
        prepareSearchQueryParams(nativeQueryStr, params, title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);
        nativeQueryStr.append(" ORDER BY d.start_date DESC ");

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        TypedQuery<Book> query = em.createQuery("FROM Book b WHERE b.bookId IN :idList ORDER BY b.deal.startDate DESC", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getBooksWithNewDealsSize(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT COUNT(DISTINCT d.id) FROM deals d LEFT JOIN books b ON d.id = b.book_id ");
        prepareSearchQueryParams(nativeQueryStr, params, title, genre, minPrice, maxPrice, minPageCount, maxPageCount, minSuggestedAge, maxSuggestedAge);

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        return ((BigInteger) nativeQuery.getSingleResult()).longValue();
    }



    @Override
    public List<Book> getWriterBooks(long writerId, String title, BookSearchOrderBy orderBy, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT b.book_id
            FROM books b LEFT OUTER JOIN deals d ON b.book_id = d.id
            WHERE b.writer_id = :writerId AND LOWER(b.title) LIKE LOWER(:title)
            ORDER BY \s
       """ + orderBy.getColumnName());
        nativeQuery.setParameter("writerId", writerId);
        nativeQuery.setParameter("title", DaoUtils.prepareSearchString(title));

        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b LEFT JOIN b.deal d WHERE b.bookId IN :idList ORDER BY " + orderBy.getColumnName(), Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getWriterBooksSize(long writerId, String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("writerId", writerId);
        params.put("title", DaoUtils.prepareSearchString(title));

        return DaoUtils.getRowCount(em, "Book b", "b.bookId"," WHERE b.writer.userId = :writerId AND LOWER(b.title) LIKE LOWER(:title) ", params);
    }

    @Override
    public List<Book> getOwnedBooks(long readerId, String title, BookSearchOrderBy orderBy, int offset, int limit, boolean isPublic) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT b.book_id
            FROM books b
            JOIN orders o ON b.book_id = o.book_id
            LEFT OUTER JOIN deals d ON b.book_id = d.id
            WHERE LOWER(b.title) LIKE LOWER(:title) AND o.status = 'COMPLETED' AND o.buyer_id = :readerId \s
        """ + (isPublic?" AND o.is_public = TRUE ":"") + " ORDER BY " + orderBy.getColumnName());
        nativeQuery.setParameter("readerId", readerId);
        nativeQuery.setParameter("title", DaoUtils.prepareSearchString(title));

        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b LEFT JOIN b.deal d WHERE b.bookId IN :idList ORDER BY " + orderBy.getColumnName(), Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getOwnedBooksSize(long readerId, String title, boolean isPublic) {

        Map<String, Object> params = new HashMap<>();
        params.put("readerId", readerId);
        params.put("title", DaoUtils.prepareSearchString(title));

        return DaoUtils.getRowCount(em,
                "Book b",
                "b.bookId",
                "WHERE LOWER(b.title) LIKE LOWER(:title) AND EXISTS(SELECT 1 FROM Order o WHERE o.book.bookId = b.bookId AND o.orderStatus = 'COMPLETED' AND o.buyer.userId = :readerId" + (isPublic?" AND o.isPublic = TRUE)":")"),
                params
        );
    }





    @Override
    public void recheckAllPaused(long userId) {
        Query query = em.createQuery("""
            UPDATE Book b SET isPaused = CASE
                WHEN NOT EXISTS(
                    SELECT 1
                    FROM BookFile bf
                    WHERE bf.id = b.bookId
                ) OR EXISTS(
                    SELECT 1
                    FROM User AS u
                    WHERE u.userId = :userId AND u.cbu IS NULL
                ) THEN TRUE
                ELSE FALSE
                END
            WHERE b.writer.userId = :userId
        """);
        query.setParameter("userId",userId);
        query.executeUpdate();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<BookGenre> getGenresByBookCount(int offset, int limit) {
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
    public List<WishlistItem> getWishlist(long userId, int offset, int limit){
        Query nativeQuery = em.createNativeQuery("SELECT book_id FROM wishlist WHERE user_id = :userId");
        nativeQuery.setParameter("userId", userId);

        TypedQuery<WishlistItem> query = em.createQuery("FROM WishlistItem w WHERE w.bookId IN :idList", WishlistItem.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getWishlistSize(long userId) {
        return DaoUtils.getRowCount(em, "WishlistItem w", "w.bookId","WHERE w.userId = :userId", Map.of("userId", userId));
    }



    @Override
    public List<Book> getBooksByWriterOrderedBySales(long writerId, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT o.book_id FROM orders o JOIN books b ON o.book_id = b.book_id
            WHERE b.writer_id = :writerId
            GROUP BY o.book_id
            ORDER BY COUNT(o.book_id) DESC, SUM(o.price) DESC
        """);
        nativeQuery.setParameter("writerId", writerId);

        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b JOIN Order o ON o.book.bookId = b.bookId WHERE b.bookId IN :idList GROUP BY b, o.book.bookId ORDER BY COUNT(o.book.bookId) DESC, SUM(o.price) DESC", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public List<Book> getBooksByWriterOrderedBySales(long writerId, int offset, int limit, int year, int month) {

        Query nativeQuery = em.createNativeQuery("""
            SELECT o.book_id FROM orders o JOIN books b ON o.book_id = b.book_id
            WHERE b.writer_id = :writerId
            AND DATE_PART('year', o.date) = :year
            AND DATE_PART('month', o.date) = :month
            GROUP BY o.book_id
            ORDER BY COUNT(o.book_id) DESC, SUM(o.price) DESC
        """);
        nativeQuery.setParameter("writerId", writerId);
        nativeQuery.setParameter("year", year);
        nativeQuery.setParameter("month", month);

        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b JOIN Order o ON o.book.bookId = b.bookId WHERE b.bookId IN :idList GROUP BY b, o.book.bookId ORDER BY COUNT(o.book.bookId) DESC, SUM(o.price) DESC", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }
}

package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.models.books.*;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
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
    public void modify(Book book, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge) {
        book.setTitle(title);
        book.setDescription(description);
        book.setGenre(genre);
        book.setPrice(price);
        book.setPageCount(pageCount);
        book.setSuggestedAge(suggestedAge);
    }

    @Override
    public void unpause(Book book){
        book.setPaused(false);
    }

    @Override
    public void updateSalesCategory(Book book, BookSalesCategory bookSalesCategory){
        book.setSalesCategory(bookSalesCategory);
    }

    private void createCoverImage(Book book, byte[] coverImage) {
        CoverImage cover = new CoverImage(book.getBookId(), coverImage);
        em.persist(cover);
    }

    private void createPreviewFile(Book book, byte[] previewFile) {
        BookPreview preview = new BookPreview(book.getBookId(), previewFile);
        em.persist(preview);
    }

    private void createBookFile(Book book, byte[] bookFile) {
        BookFile file = new BookFile(book.getBookId(), bookFile);
        em.persist(file);
    }

    @Override
    public void createOrUpdateCoverImage(Book book, byte[] coverImage) {
        if (book.getCoverImage()==null){
            createCoverImage(book, coverImage);
        } else {
            book.getCoverImage().setFile(coverImage);
        }
    }

    @Override
    public void createOrUpdatePreview(Book book, byte[] preview) {
        if (book.getPreview()==null){
            createPreviewFile(book, preview);
        } else {
            book.getPreview().setFile(preview);
        }
    }

    @Override
    public void createOrUpdateBookFile(Book book, byte[] bookFile) {
        if (book.getBookFile()==null){
            createBookFile(book, bookFile);
        } else {
            book.getBookFile().setFile(bookFile);
        }
    }


    @Override
    public List<Book> searchWithParams(BookSearchQueryDTO queryDTO) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT b.book_id FROM books b LEFT JOIN deals d ON b.book_id = d.id ");
        prepareSearchQueryParams(nativeQueryStr, params, queryDTO);
        nativeQueryStr.append(" ORDER BY ").append(queryDTO.getOrderBy().getColumnName());

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }

       TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b LEFT JOIN b.deal d WHERE b.bookId IN :idList ORDER BY " + queryDTO.getOrderBy().getColumnName(), Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, queryDTO.getOffset(), queryDTO.getLimit());
    }

    @Override
    public long getSearchSize(BookSearchQueryDTO queryDTO) {
        StringBuilder nativeQueryStr = new StringBuilder("SELECT COUNT(DISTINCT b.book_id) FROM books b LEFT JOIN deals d ON b.book_id = d.id ");

        return sizeQuery(nativeQueryStr, queryDTO);
    }

    @Override
    public List<Book> getRecommendationsForBook(BookSearchQueryDTO queryDTO) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (queryDTO.getOrderBy() == null){
            queryDTO.setOrderBy(BookSearchOrderBy.BEST_SELLERS);
        }

        Book book = findById(queryDTO.getRecommendationsForId()).orElseThrow(BookNotFoundException::new);
        queryDTO.setGenre(null);
        queryDTO.setWriterId(null);

        nativeQueryStr.append("SELECT b.book_id FROM books b LEFT JOIN deals d ON b.book_id = d.id JOIN users u ON b.writer_id = u.user_id LEFT JOIN orders o ON b.book_id = o.book_id AND o.status = 'COMPLETED' ");
        prepareSearchQueryParams(nativeQueryStr, params, queryDTO);
        nativeQueryStr.append(" AND (b.genre = :genre OR b.writer_id = :writerId) AND b.book_id <> :bookId GROUP BY b.book_id, u.user_id, d.start_date ");
        nativeQueryStr.append(" ORDER BY ").append(queryDTO.getOrderBy().getColumnName());

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }
        nativeQuery.setParameter("genre", book.getGenre().toString());
        nativeQuery.setParameter("writerId", book.getWriter().getUserId());
        nativeQuery.setParameter("bookId", book.getBookId());

        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b LEFT JOIN b.deal d WHERE b.bookId IN :idList", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, queryDTO.getOffset(), queryDTO.getLimit());
    }

    @Override
    public long getRecommendationsForBookSize(BookSearchQueryDTO queryDTO) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        Book book = findById(queryDTO.getRecommendationsForId())
                .orElseThrow(BookNotFoundException::new);
        BookGenre genre = queryDTO.getGenre();
        queryDTO.setGenre(null);

        nativeQueryStr.append("SELECT COUNT(DISTINCT b.book_id) FROM books b LEFT JOIN deals d ON b.book_id = d.id JOIN users u ON b.writer_id = u.user_id LEFT JOIN orders o ON b.book_id = o.book_id AND o.status = 'COMPLETED' ");
        prepareSearchQueryParams(nativeQueryStr, params, queryDTO);
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
    public List<Book> getTopBooks(BookSearchQueryDTO queryDTO) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT o.book_id FROM orders o LEFT JOIN books b ON o.book_id = b.book_id LEFT JOIN deals d ON d.id = b.book_id ");
        prepareSearchQueryParams(nativeQueryStr, params, queryDTO);
        nativeQueryStr.append(" GROUP BY o.book_id ");

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b JOIN Order o ON o.book.bookId = b.bookId WHERE b.bookId IN :idList GROUP BY b, o.book.bookId ORDER BY COUNT(o.book.bookId) DESC", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, queryDTO.getOffset(), queryDTO.getLimit());
    }

    @Override
    public long getTopBooksSize(BookSearchQueryDTO queryDTO) {
        StringBuilder nativeQueryStr = new StringBuilder("SELECT COUNT(DISTINCT o.book_id) FROM orders o LEFT JOIN books b ON o.book_id = b.book_id LEFT JOIN deals d ON d.id = b.book_id ");

        return sizeQuery(nativeQueryStr, queryDTO);
    }


    @Override
    public List<Book> getBooksWithNewDeals(BookSearchQueryDTO queryDTO) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append(" SELECT d.id FROM deals d LEFT JOIN books b ON b.book_id = d.id ");
        prepareSearchQueryParams(nativeQueryStr, params, queryDTO);
        nativeQueryStr.append(" ORDER BY d.start_date DESC ");

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        params.forEach(nativeQuery::setParameter);

        TypedQuery<Book> query = em.createQuery("FROM Book b WHERE b.bookId IN :idList ORDER BY b.deal.startDate DESC", Book.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, queryDTO.getOffset(), queryDTO.getLimit());
    }


    @Override
    public long getBooksWithNewDealsSize(BookSearchQueryDTO queryDTO) {
        StringBuilder nativeQueryStr = new StringBuilder("SELECT COUNT(DISTINCT d.id) FROM deals d LEFT JOIN books b ON d.id = b.book_id ");

        return sizeQuery(nativeQueryStr, queryDTO);
    }

    private void prepareSearchQueryParams(StringBuilder query, Map<String, Object> params, BookSearchQueryDTO queryDTO){
        query.append(" WHERE b.is_paused = FALSE AND LOWER(title) LIKE LOWER(:title) ");
        params.put("title", DaoUtils.prepareSearchString(queryDTO.getTitle()));
        DaoUtils.addQueryCondition(query, " AND ((d.price IS NULL AND b.price >= :minPrice) OR d.price >= :minPrice ) ", params, "minPrice", queryDTO.getMinPrice());
        DaoUtils.addQueryCondition(query, " AND ((d.price IS NULL AND b.price <= :maxPrice) OR d.price <= :maxPrice ) ", params, "maxPrice", queryDTO.getMaxPrice());
        DaoUtils.addQueryCondition(query, " AND b.page_count >= :minPageCount ", params, "minPageCount", queryDTO.getMinPageCount());
        DaoUtils.addQueryCondition(query, " AND b.page_count <= :maxPageCount ", params, "maxPageCount", queryDTO.getMaxPageCount());
        DaoUtils.addQueryCondition(query, " AND b.suggested_age >= :minSuggestedAge ", params, "minSuggestedAge", queryDTO.getMinSuggestedAge());
        DaoUtils.addQueryCondition(query, " AND b.suggested_age <= :maxSuggestedAge ", params, "maxSuggestedAge", queryDTO.getMaxSuggestedAge());
        DaoUtils.addQueryCondition(query, " AND b.writer_id = :writerId ", params, "writerId", queryDTO.getWriterId());
        if (queryDTO.isRecommendedByUserOnly()) {
            DaoUtils.addQueryCondition(query, " AND EXISTS (SELECT 1 FROM recommendations rec WHERE rec.user_id = :ownerId AND rec.book_id = b.book_id ) ", params, "ownerId", queryDTO.getOwnerId());
        } else {
            DaoUtils.addQueryCondition(query, " AND EXISTS (SELECT 1 FROM orders o WHERE o.buyer_id = :ownerId AND o.book_id = b.book_id AND o.status = 'COMPLETED') ", params, "ownerId", queryDTO.getOwnerId());
        }
        if (queryDTO.getGenre() != null) {
            DaoUtils.addQueryCondition(query, " AND b.genre = :genre ", params, "genre", queryDTO.getGenre().toString());
        }
    }

    private long sizeQuery(StringBuilder query, BookSearchQueryDTO queryDTO){
        Map<String, Object> params = new HashMap<>();

        prepareSearchQueryParams(query, params, queryDTO);

        Query nativeQuery = em.createNativeQuery(query.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        return ((BigInteger) nativeQuery.getSingleResult()).longValue();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<BookGenre> getGenresByBookCount(int offset, int limit) {
        Query query = em.createNativeQuery("SELECT genre FROM books GROUP BY genre ORDER BY COUNT(*) DESC");
        query.setMaxResults(limit);
        query.setFirstResult(offset);

        return (List<BookGenre>) query.getResultStream().map(genre -> BookGenre.valueOf((String) genre)).collect(Collectors.toList());
    }

}

package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class OrderJpaDao implements OrderDao {

    @PersistenceContext
    private EntityManager em;

    private void prepareSearchParams(StringBuilder query, Map<String, Object> params, Long bookId, Long writerId, Long readerId, String title, OrderStatus orderStatus){
        query.append("WHERE LOWER(b.title) LIKE LOWER(:title)");
        params.put("title", DaoUtils.prepareSearchString(title));
        if (bookId != null){
            query.append(" AND b.book_id = :bookId ");
            params.put("bookId", bookId);
        }
        if (writerId != null){
            query.append(" AND b.writer_id = :writerId ");
            params.put("writerId", writerId);
        }
        if (readerId != null){
            query.append(" AND o.buyer_id = :readerId ");
            params.put("readerId", readerId);
        }
        if (orderStatus != null){
            query.append(" AND o.status = orderStatus ");
            params.put("orderStatus", orderStatus.toString());
        }
    }

    @Override
    public List<Order> getAllOrders(Long bookId, Long writerId, Long readerId, String title, OrderStatus orderStatus, int offset, int limit) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT o.order_id FROM orders o JOIN books b ON o.book_id = b.book_id ");
        prepareSearchParams(nativeQueryStr, params, bookId, writerId, readerId, title, orderStatus);
        nativeQueryStr.append(" ORDER BY o.date DESC ");

        Query nativeQuery = em.createNativeQuery(nativeQueryStr.toString());
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            nativeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.orderId IN :idList ORDER BY o.date DESC", Order.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getAllOrdersSize(Long bookId, Long writerId, Long readerId, String title, OrderStatus orderStatus) {
        StringBuilder nativeQueryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        nativeQueryStr.append("SELECT COUNT(DISTINCT o.order_id) FROM orders o JOIN books b ON o.book_id = b.book_id ");
        prepareSearchParams(nativeQueryStr, params, bookId, writerId, readerId, title, orderStatus);

        Query query = em.createNativeQuery(nativeQueryStr.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    @Override
    public Optional<Order> find(long buyerId, long bookId) {
        TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.buyer.userId = :buyerId AND o.book.bookId = :bookId", Order.class);
        query.setParameter("buyerId", buyerId);
        query.setParameter("bookId", bookId);
        Order order;
        try {
            order = query.getSingleResult();
        } catch (NoResultException e) {
            order = null;
        }
        return Optional.ofNullable(order);
    }

    @Override
    public Optional<Order> findById(long orderId) {
        return Optional.ofNullable(em.find(Order.class, orderId));
    }

    @Override
    public Order create(User buyer, Book book, OrderStatus orderStatus, LocalDateTime date, BigDecimal price) {
        Order order = new Order(buyer, book, orderStatus, date, price);
        em.persist(order);
        return order;
    }

    @Override
    public void update(Order order, OrderStatus orderStatus, LocalDateTime date) {
        order.setOrderStatus(orderStatus);
        order.setDate(date);
    }

    @Override
    public void update(Order order, OrderStatus orderStatus, LocalDateTime date, String rejectedReason){
        update(order, orderStatus, date);
        order.setRejectedReason(rejectedReason);
    }


    private PaymentReceipt createPaymentReceipt(Order order, byte[] paymentReceipt, String type) {
        PaymentReceipt receipt = new PaymentReceipt(order.getOrderId(), paymentReceipt, type);
        em.persist(receipt);
        return receipt;
    }


    private void updatePaymentReceipt(Order order, byte[] paymentReceipt, String type) {
        order.getPaymentReceipt().setFile(paymentReceipt);
        order.getPaymentReceipt().setType(type);
    }

    @Override
    public PaymentReceipt createOrUpdatePaymentReceipt(Order order, byte[] paymentReceipt, String type) {
        if (order.getPaymentReceipt() == null) {
            return createPaymentReceipt(order, paymentReceipt, type);
        } else {
            updatePaymentReceipt(order, paymentReceipt, type);
            return order.getPaymentReceipt();
        }
    }

    @Override
    public List<Order> getReaderOrders(long readerId, String title, OrderStatus orderStatus, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT o.order_id
            FROM orders o
            JOIN books b ON o.book_id = b.book_id
            WHERE LOWER(b.title) LIKE LOWER(:title) AND o.buyer_id = :readerId
        """ + (orderStatus!=null?" AND o.status = :status":"") + (" ORDER BY o.date DESC"));
        nativeQuery.setParameter("title", DaoUtils.prepareSearchString(title));
        nativeQuery.setParameter("readerId", readerId);
        if (orderStatus != null) {
            nativeQuery.setParameter("status", orderStatus.toString());
        }

        TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.orderId IN :idList ORDER BY o.date DESC", Order.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getReaderOrdersSize(long readerId, String title, OrderStatus orderStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", DaoUtils.prepareSearchString(title));
        params.put("readerId", readerId);
        if (orderStatus != null){
            params.put("status", orderStatus);
        }

        return DaoUtils.getRowCount(em,
                "Order o LEFT JOIN Book b ON o.book.bookId = b.bookId",
                "o.orderId",
                "WHERE LOWER(b.title) LIKE LOWER(:title) AND o.buyer.userId = :readerId" + (orderStatus!=null?" AND o.orderStatus = :status":""),
                params
        );
    }

    @Override
    public List<Order> getWriterOrders(long writerId, String title, OrderStatus orderStatus, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT o.order_id
            FROM orders o
            JOIN books b ON o.book_id = b.book_id
            WHERE LOWER(b.title) LIKE LOWER(:title) AND b.writer_id = :writerId
        """ + (orderStatus!=null?" AND o.status = :status":"") + (" ORDER BY o.date DESC"));
        nativeQuery.setParameter("title", DaoUtils.prepareSearchString(title));
        nativeQuery.setParameter("writerId", writerId);
        if (orderStatus != null) {
            nativeQuery.setParameter("status", orderStatus.toString());
        }

        TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.orderId IN :idList ORDER BY o.date DESC", Order.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getWriterOrdersSize(long writerId, String title, OrderStatus orderStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", DaoUtils.prepareSearchString(title));
        params.put("writerId", writerId);
        if (orderStatus != null){
            params.put("status", orderStatus);
        }

        return DaoUtils.getRowCount(em,
            "Order o LEFT JOIN Book b ON o.book.bookId = b.bookId",
            "o.orderId",
            "WHERE LOWER(b.title) LIKE LOWER(:title) AND b.writer.userId = :writerId" + (orderStatus!=null?" AND o.orderStatus = :status":""),
             params
        );
    }

    @Override
    public boolean ownsBook(long bookId, String email) {
        Query query = em.createQuery("SELECT 1 FROM Order o WHERE EXISTS (SELECT 1 FROM Order o WHERE o.book.bookId = :bookId AND o.buyer.email = :email AND o.orderStatus = 'COMPLETED')");
        query.setParameter("bookId", bookId);
        query.setParameter("email", email);
        query.setMaxResults(1);

        try {
            return query.getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public long getTotalOrdersForBook(long bookId) {
        return DaoUtils.getRowCount(em,"Order o", "o.orderId", "WHERE o.book.bookId = :bookId AND o.orderStatus = 'COMPLETED'", Map.of("bookId", bookId));
    }

    @Override
    public BigDecimal getTotalSales(long writerId) {
        Query query = em.createQuery("SELECT COALESCE(SUM(o.price), 0) FROM Order o WHERE o.book.writer.userId = :writerId AND o.orderStatus = 'COMPLETED'");
        query.setParameter("writerId", writerId);

        return (BigDecimal) query.getSingleResult();
    }

    @Override
    public BigDecimal getTotalSalesForBook(long bookId) {
        Query query = em.createQuery("SELECT COALESCE(SUM(o.price), 0) FROM Order o WHERE o.book.bookId = :bookId AND o.orderStatus = 'COMPLETED'");
        query.setParameter("bookId", bookId);

        return (BigDecimal) query.getSingleResult();
    }

    @Override
    public BigDecimal getWriterTotalSalesPerMonth(long writerId, int year, int month) {
        Query query = em.createQuery(
        """
                SELECT COALESCE(SUM(o.price), 0) FROM Order o
                WHERE o.book.writer.userId = :writerId
                AND o.orderStatus = 'COMPLETED'
                AND FUNCTION('YEAR', o.date) = :year
                AND FUNCTION('MONTH', o.date) = :month
           """
        );
        query.setParameter("writerId", writerId);
        query.setParameter("year", year);
        query.setParameter("month", month);

        return (BigDecimal) query.getSingleResult();
    }

    @Override
    public BigDecimal getBookTotalSalesPerMonth(long bookId, int year, int month) {
        Query query = em.createQuery(
        """
                SELECT COALESCE(SUM(o.price), 0) FROM Order o
                WHERE o.book.bookId = :bookId
                AND o.orderStatus = 'COMPLETED'
                AND FUNCTION('YEAR', o.date) = :year
                AND FUNCTION('MONTH', o.date) = :month
          """
        );
        query.setParameter("bookId", bookId);
        query.setParameter("year", year);
        query.setParameter("month", month);

        return (BigDecimal) query.getSingleResult();
    }

    @Override
    public long getBookTotalOrdersPerMonth(long bookId, int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("bookId", bookId);
        params.put("year", year);
        params.put("month", month);
        return DaoUtils.getRowCount(
                em,
                "Order o",
                "o.orderId",
                "WHERE o.book.bookId = :bookId AND FUNCTION('YEAR', o.date) = :year AND FUNCTION('MONTH', o.date) = :month AND o.orderStatus = 'COMPLETED'",
                params);
    }

    @Override
    public long getWriterTotalOrdersPerMonth(long writerId, int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("writerId", writerId);
        params.put("year", year);
        params.put("month", month);
        return DaoUtils.getRowCount(
                em,
                "Order o",
                "o.orderId",
                "WHERE o.book.writer.userId = :writerId AND FUNCTION('YEAR', o.date) = :year AND FUNCTION('MONTH', o.date) = :month AND o.orderStatus = 'COMPLETED'",
                params);
    }


    @Override
    public long getBooksByWriterOrderedSize(long writerId){
        return DaoUtils.getRowCount(em, "Order o", "o.book.bookId", "WHERE o.book.writer.userId = :writerId AND o.orderStatus = 'COMPLETED'", Map.of("writerId", writerId));
    }

    @Override
    public long getBooksByWriterOrderedSize(long writerId, int year, int month){
        Map<String, Object> params = new HashMap<>();
        params.put("writerId", writerId);
        params.put("year", year);
        params.put("month", month);

        return DaoUtils.getRowCount(
                em,
                "Order o",
                "o.book.bookId",
                "WHERE o.book.writer.userId = :writerId AND FUNCTION('YEAR', o.date) = :year AND FUNCTION('MONTH', o.date) = :month AND o.orderStatus = 'COMPLETED'",
                params
        );
    }
}
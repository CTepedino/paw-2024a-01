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
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class OrderJpaDao implements OrderDao {

    @PersistenceContext
    private EntityManager em;

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
    public Order create(User buyer, Book book, OrderStatus orderStatus, LocalDateTime date, boolean isPublic, BigDecimal price) {
        Order order = new Order(buyer, book, orderStatus, date, isPublic, price);
        em.persist(order);
        return order;
    }

    @Override
    public void update(Order order, OrderStatus orderStatus, LocalDateTime date, boolean isPublic) {
        order.setOrderStatus(orderStatus);
        order.setDate(date);
        order.setPublic(isPublic);
    }

    @Override
    public void update(Order order, OrderStatus orderStatus, LocalDateTime date, boolean isPublic, String rejectedReason){
        update(order, orderStatus, date, isPublic);
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
        """ + (orderStatus!=null?" AND o.status = :status":""));
        nativeQuery.setParameter("title", DaoUtils.prepareSearchString(title));
        nativeQuery.setParameter("readerId", readerId);
        if (orderStatus != null) {
            nativeQuery.setParameter("status", orderStatus.toString());
        }

        TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.orderId IN :idList", Order.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getReaderOrdersSize(long readerId, String title, OrderStatus orderStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", DaoUtils.prepareSearchString(title));
        params.put("readerId", readerId);
        if (orderStatus != null){
            params.put("status", orderStatus.toString());
        }

        return DaoUtils.getRowCount(em,
                "Order o LEFT JOIN Book b ON o.book.bookId = b.bookId",
                "o.orderId",
                "WHERE LOWER(b.title) LIKE LOWER(:title) AND o.buyer.userId = :readerId" + (orderStatus!=null?" AND o.status = :status":""),
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
        """ + (orderStatus!=null?" AND o.status = :status":""));
        nativeQuery.setParameter("title", DaoUtils.prepareSearchString(title));
        nativeQuery.setParameter("writerId", writerId);
        if (orderStatus != null) {
            nativeQuery.setParameter("status", orderStatus.toString());
        }

        TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.orderId IN :idList", Order.class);

        return DaoUtils.paginatedQuery(em, nativeQuery, query, offset, limit);
    }

    @Override
    public long getWriterOrdersSize(long writerId, String title, OrderStatus orderStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", DaoUtils.prepareSearchString(title));
        params.put("writerId", writerId);
        if (orderStatus != null){
            params.put("status", orderStatus.toString());
        }

        return DaoUtils.getRowCount(em,
            "Order o LEFT JOIN Book b ON o.book.bookId = b.bookId",
            "o.orderId",
            "WHERE LOWER(b.title) LIKE LOWER(:title) AND b.writer.userId = :writerId" + (orderStatus!=null?" AND o.status = :status":""),
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
        return DaoUtils.getRowCount(em,"Order o", "o.orderId", "WHERE o.book.bookId = :bookId", Map.of("bookId", bookId));
    }

    @Override
    public BigDecimal getTotalSales(long writerId) {
        Query query = em.createQuery("SELECT SUM(o.price) FROM Order o WHERE o.book.writer.userId = :writerId");
        query.setParameter("writerId", writerId);
        return (BigDecimal) query.getSingleResult();
    }

    @Override
    public BigDecimal getTotalSalesForBook(long bookId) {
        Query query = em.createQuery("SELECT SUM(o.price) FROM Order o WHERE o.book.bookId = :bookId");
        query.setParameter("bookId", bookId);
        return (BigDecimal) query.getSingleResult();
    }

    @Override
    public BigDecimal getTotalSalesForMonth(long writerId, int year, int month) {
        Query query = em.createQuery(
                "SELECT SUM(o.price) FROM Order o " +
                        "WHERE o.book.writer.userId = :writerId " +
                        "AND FUNCTION('YEAR', o.date) = :year " +
                        "AND FUNCTION('MONTH', o.date) = :month"
        );
        query.setParameter("writerId", writerId);
        query.setParameter("year", year);
        query.setParameter("month", month);
        return (BigDecimal) query.getSingleResult();
    }

    @Override
    public BigDecimal getTotalSalesForMonthForBook(long bookId, int year, int month) {
        Query query = em.createQuery(
                "SELECT SUM(o.price) FROM Order o " +
                        "WHERE o.book.bookId = :bookId " +
                        "AND FUNCTION('YEAR', o.date) = :year " +
                        "AND FUNCTION('MONTH', o.date) = :month"
        );
        query.setParameter("bookId", bookId);
        query.setParameter("year", year);
        query.setParameter("month", month);
        return (BigDecimal) query.getSingleResult();
    }

    @Override
    public long getTotalOrdersForMonthForBook(long bookId, int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("bookId", bookId);
        params.put("year", year);
        params.put("month", month);
        return DaoUtils.getRowCount(
                em,
                "Order o",
                "o.orderId",
                "WHERE o.book.bookId = :bookId AND FUNCTION('YEAR', o.date) = :year AND FUNCTION('MONTH', o.date) = :month",
                params);
    }

    @Override
    public long getTotalOrdersForMonthForWriter(long writerId, int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("writerId", writerId);
        params.put("year", year);
        params.put("month", month);
        return DaoUtils.getRowCount(
                em,
                "Order o",
                "o.orderId",
                "WHERE o.book.writer.userId = :writerId AND FUNCTION('YEAR', o.date) = :year AND FUNCTION('MONTH', o.date) = :month",
                params);
    }


    @Override
    public long getBooksByWriterOrderedSize(long writerId){
        return DaoUtils.getRowCount(em, "Order o", "o.book.bookId", "WHERE o.book.writer.userId = :writerId", Map.of("writerId", writerId));
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
                "WHERE o.book.writer.userId = :writerId AND FUNCTION('YEAR', o.date) = :year AND FUNCTION('MONTH', o.date) = :month",
                params
        );
    }
}
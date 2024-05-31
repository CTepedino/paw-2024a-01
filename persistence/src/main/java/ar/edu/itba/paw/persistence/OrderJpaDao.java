package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
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
    public Order create(User buyer, Book book, OrderStatus orderStatus, LocalDateTime date, boolean isPublic) {
        Order order = new Order(buyer, book, orderStatus, date, isPublic);
        em.persist(order);
        return order;
    }

    @Override
    public void update(long orderId, OrderStatus orderStatus, LocalDateTime date, boolean isPublic) {
        findById(orderId).ifPresent(order -> {
            order.setOrderStatus(orderStatus);
            order.setDate(date);
            order.setPublic(isPublic);
            em.merge(order);
        });
    }

    @Override
    public PaymentReceipt createPaymentReceipt(Order order, byte[] paymentReceipt, String type) {
        PaymentReceipt receipt = new PaymentReceipt(order.getOrderId(), paymentReceipt, type);
        em.persist(receipt);
        return receipt;
    }

    @Override
    public void updatePaymentReceipt(Order order, byte[] paymentReceipt, String type) {
        order.getPaymentReceipt().setFile(paymentReceipt);
        order.getPaymentReceipt().setType(type);
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
                "orders o LEFT JOIN books b ON o.book_id = b.book_id",
                "WHERE LOWER(b.title) LIKE LOWER(:title) AND o.buyer_id = :readerId" + (orderStatus!=null?" AND o.status = :status":""),
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
            "orders o LEFT JOIN books b ON o.book_id = b.book_id",
            "WHERE LOWER(b.title) LIKE LOWER(:title) AND b.writer_id = :writerId" + (orderStatus!=null?" AND o.status = :status":""),
             params
        );
    }

    @Override
    public boolean ownsBook(long bookId, String email) {
        Query query = em.createQuery("SELECT 1 FROM Order o WHERE EXISTS (SELECT 1 FROM Order o WHERE o.book.bookId = :bookId AND o.buyer.email = :email)");
        query.setParameter("bookId", bookId);
        query.setParameter("email", email);
        query.setMaxResults(1);

        try {
            return query.getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }
    }
}
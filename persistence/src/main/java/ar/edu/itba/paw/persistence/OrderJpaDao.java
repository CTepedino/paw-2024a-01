package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public long create(User buyer, Book book, OrderStatus orderStatus, LocalDateTime date, boolean isPublic) {
        Order order = new Order(buyer, book, orderStatus, date, isPublic);
        em.persist(order);
        return order.getOrderId();
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
    public List<Order> getReaderOrders(long readerId, String title, OrderStatus orderStatus, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT o.order_id
            FROM orders o
            JOIN books b ON o.book_id = b.book_id
            WHERE LOWER(b.title) LIKE LOWER(:title) AND o.buyer_id = :readerId
        """ + (orderStatus!=null?" AND o.status = :status":""));
        nativeQuery.setParameter("title", "%" + DaoUtils.escapeSearchString(title) + "%");
        nativeQuery.setParameter("readerId", readerId);
        if (orderStatus != null) {
            nativeQuery.setParameter("status", orderStatus.toString());
        }
        nativeQuery.setFirstResult(offset);
        nativeQuery.setMaxResults(limit);

        @SuppressWarnings("unchecked")
        final List<Long> idList = (List<Long>) nativeQuery.getResultStream().map(n -> (Long)((Number)n).longValue()).collect(Collectors.toList());

        if (idList.isEmpty()){
            return Collections.emptyList();
        }

        TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.orderId IN :idList", Order.class);
        query.setParameter("idList", idList);
        return query.getResultList();
    }

    @Override
    public long getReaderOrdersSize(long readerId, String title, OrderStatus orderStatus) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT COUNT(DISTINCT o.order_id)
            FROM orders o
            JOIN books b ON o.book_id = b.book_id
            WHERE LOWER(b.title) LIKE LOWER(:title) AND o.buyer_id = :readerId
        """ + (orderStatus!=null?" AND o.status = :status":""));
        nativeQuery.setParameter("title", "%" + DaoUtils.escapeSearchString(title) + "%");
        nativeQuery.setParameter("readerId", readerId);
        if (orderStatus != null) {
            nativeQuery.setParameter("status", orderStatus.toString());
        }

        return ((BigInteger) nativeQuery.getSingleResult()).longValue();
    }

    @Override
    public List<Order> getWriterOrders(long writerId, String title, OrderStatus orderStatus, int offset, int limit) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT o.order_id
            FROM orders o
            JOIN books b ON o.book_id = b.book_id
            WHERE LOWER(b.title) LIKE LOWER(:title) AND b.writer_id = :writerId
        """ + (orderStatus!=null?" AND o.status = :status":""));
        nativeQuery.setParameter("title", "%" + DaoUtils.escapeSearchString(title) + "%");
        nativeQuery.setParameter("writerId", writerId);
        if (orderStatus != null) {
            nativeQuery.setParameter("status", orderStatus.toString());
        }
        nativeQuery.setFirstResult(offset);
        nativeQuery.setMaxResults(limit);

        @SuppressWarnings("unchecked")
        final List<Long> idList = (List<Long>) nativeQuery.getResultStream().map(n -> (Long)((Number)n).longValue()).collect(Collectors.toList());

        if (idList.isEmpty()){
            return Collections.emptyList();
        }

        TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.orderId IN :idList", Order.class);
        query.setParameter("idList", idList);
        return query.getResultList();
    }

    @Override
    public long getWriterOrdersSize(long writerId, String title, OrderStatus orderStatus) {
        Query nativeQuery = em.createNativeQuery("""
            SELECT COUNT(DISTINCT o.order_id)
            FROM orders o
            JOIN books b ON o.book_id = b.book_id
            WHERE LOWER(b.title) LIKE LOWER(:title) AND b.writer_id = :writerId
        """ + (orderStatus!=null?" AND o.status = :status":""));
        nativeQuery.setParameter("title", "%" + DaoUtils.escapeSearchString(title) + "%");
        nativeQuery.setParameter("writerId", writerId);
        if (orderStatus != null) {
            nativeQuery.setParameter("status", orderStatus.toString());
        }

        return ((BigInteger) nativeQuery.getSingleResult()).longValue();
    }

    @Override
    public boolean ownsBook(long bookId, String email) {
        Query query = em.createQuery("SELECT 1 FROM Order o WHERE EXISTS (SELECT 1 FROM Order o WHERE o.book.bookId = :bookId AND o.buyer.email = :email)");
        query.setParameter("bookId", bookId);
        query.setParameter("email", email);
        query.setMaxResults(1);
        boolean ownsBook;
        try {
           ownsBook = query.getSingleResult() !=null;
        } catch (NoResultException e) {
            ownsBook = false;
        }

        return ownsBook;
    }
}

package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderJpaDao implements OrderDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Order> find(long buyerId, long bookId) {
        TypedQuery<Order> query = em.createQuery("FROM Order o WHERE o.buyer.userId = :buyerId AND o.book.bookId = :bookId", Order.class);
        query.setParameter("buyerId", buyerId);
        query.setParameter("bookId", bookId);
        return Optional.ofNullable(query.getSingleResult());
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
        return List.of();
    }

    @Override
    public long getReaderOrdersSize(long readerId, String title, OrderStatus orderStatus) {
        return 0;
    }

    @Override
    public List<Order> getWriterOrders(long writerId, String title, OrderStatus orderStatus, int offset, int limit) {
        return List.of();
    }

    @Override
    public long getWriterOrdersSize(long writerId, String title, OrderStatus orderStatus) {
        return 0;
    }

    @Override
    public boolean ownsBook(long bookId, String email) {
        TypedQuery<Boolean> query = em.createQuery("SELECT EXISTS (SELECT 1 FROM Order o WHERE o.book.bookId = :bookId AND o.buyer.email = :email)", Boolean.class);
        query.setParameter("bookId", bookId);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
}

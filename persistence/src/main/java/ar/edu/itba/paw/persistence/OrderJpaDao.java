package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderJpaDao implements OrderDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Order> find(long buyerId, long bookId) {
        return Optional.empty();
    }

    @Override
    public Optional<Order> findById(long orderId) {
        return Optional.empty();
    }

    @Override
    public long create(long buyerId, long bookId, OrderStatus orderStatus) {
        return 0;
    }

    @Override
    public void update(long orderId, OrderStatus orderStatus) {

    }

    @Override
    public void recommendBook(long orderId, boolean isRecommended) {

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
        return false;
    }
}

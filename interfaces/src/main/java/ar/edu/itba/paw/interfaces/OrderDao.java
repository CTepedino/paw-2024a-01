package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    Optional<Order> find(long buyerId, long writerId, long bookId);

    void create(long buyerId, long writerId, long bookId, OrderStatus orderStatus);

    void setStatus(long buyerId, long writerId, long bookId, OrderStatus orderStatus);

    List<Order> getAllReaderOrders(long readerId);

    List<Order> getAllNonCompleteReaderOrders(long readerId);

    List<Order> getAllWriterOrders(long writerId);

    List<Order> getAllNonCompleteWriterOrders(long writerId);
}

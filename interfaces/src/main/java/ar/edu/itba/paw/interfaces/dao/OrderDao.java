package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    Optional<Order> find(long buyerId, long bookId);

    Optional<Order> findById(long orderId);

    long create(long buyerId, long bookId, OrderStatus orderStatus);

    void setStatus(long orderId, OrderStatus orderStatus);

    List<Order> getAllReaderOrders(long readerId);

    List<Order> getAllWriterOrders(long writerId);

}

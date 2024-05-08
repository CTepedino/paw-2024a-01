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

    List<Order> getAllReaderOrders(long readerId, int offset, int limit);
    long getAllReaderOrdersSize(long readerId);

    List<Order> getReaderOrdersWithParams(long readerId,  String title, OrderStatus orderStatus, int offset, int limit);

    List<Order> getAllWriterOrders(long writerId, int offset, int limit);
    long getAllWriterOrdersSize(long writerId);

    List<Order> getWriterOrdersWithParams(long writerId,  String title, OrderStatus orderStatus, int offset, int limit);
}

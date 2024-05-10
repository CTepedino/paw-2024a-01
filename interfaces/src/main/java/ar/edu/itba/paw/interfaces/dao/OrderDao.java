package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderOrderBy;
import ar.edu.itba.paw.models.orders.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    Optional<Order> find(long buyerId, long bookId);

    Optional<Order> findById(long orderId);

    long create(long buyerId, long bookId, OrderStatus orderStatus);

    void update(long orderId, OrderStatus orderStatus);

    List<Order> getReaderOrders(long readerId, String title, OrderStatus orderStatus, OrderOrderBy orderBy, int offset, int limit);
    long getReaderOrdersSize(long readerId, String title, OrderStatus orderStatus);

    List<Order> getWriterOrders(long writerId, String title, OrderStatus orderStatus, OrderOrderBy orderBy, int offset, int limit);
    long getWriterOrdersSize(long writerId, String title, OrderStatus orderStatus);

    void updateAllWriterOrders(long writerId, OrderStatus oldStatus,OrderStatus newStatus);

    boolean hasBookFileAccess(long bookId, String email);
}

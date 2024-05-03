package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.orders.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    void create(long bookId);

    boolean canCreateOrder(long bookId);

    Optional<Order> find(long buyerId, long writerId, long bookId);
    Order toNextStatus(Order order);

    List<Order> getAllReaderOrders(long readerId);

    List<Order> getAllNonCompleteReaderOrders(long readerId);

    List<Order> getAllWriterOrders(long writerId);

    List<Order> getAllNonCompleteWriterOrders(long writerId);

}

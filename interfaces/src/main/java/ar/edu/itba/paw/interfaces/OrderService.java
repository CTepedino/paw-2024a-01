package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    void create(long buyerId, long writerId, long bookId);

    Optional<Order> find(long buyerId, long writerId, long bookId);
    Order toNextStatus(Order order);

    List<Order> getAllReaderOrders(long readerId);

    List<Order> getAllNonCompleteReaderOrders(long readerId);

    List<Order> getAllWriterOrders(long writerId);

    List<Order> getAllNonCompleteWriterOrders(long writerId);

}

package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.OrderStatus;

import java.util.Optional;

public interface OrderDao {

    Optional<Order> find(long buyerId, long writerId, long bookId);

    Order create(long buyerId, long writerId, long bookId, OrderStatus orderStatus);

    void setStatus(long buyerId, long writerId, long bookId, OrderStatus orderStatus);
}

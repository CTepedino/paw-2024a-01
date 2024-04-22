package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Order;

import java.time.LocalDate;
import java.util.Optional;

public interface OrderService {

    Order create(long buyerId, long writerId, long bookId);

    Optional<Order> find(long buyerId, long writerId, long bookId);

    Order updateToWaitingPayment(Order order);

    Order updateToWaitingForBook(Order order);

    Order updateToCompleted(Order order);
}

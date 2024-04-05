package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.PaymentMethod;

import java.time.LocalDate;
import java.util.Optional;

public interface OrderDao {

    Optional<Order> findById(long id);

    Order create(long bookId, long buyerId, PaymentMethod paymentMethod, LocalDate date);
}

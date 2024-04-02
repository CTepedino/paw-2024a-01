package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.OrderDao;
import ar.edu.itba.paw.interfaces.OrderService;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(final OrderDao orderDao){
        this.orderDao = orderDao;
    }

    @Override
    public Optional<Order> findById(long id) {
        return orderDao.findById(id);
    }

    @Override
    public Order create(long bookId, long buyerId, PaymentMethod paymentMethod, LocalDate date) {
        return orderDao.create(bookId, buyerId, paymentMethod, date);
    }
}

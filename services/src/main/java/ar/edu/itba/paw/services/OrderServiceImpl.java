package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.OrderDao;
import ar.edu.itba.paw.interfaces.OrderService;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.OrderStatus;
import ar.edu.itba.paw.models.exception.IllegalOrderStatusUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(final OrderDao orderDao){
        this.orderDao = orderDao;
    }

    @Override
    public Order create(long buyerId, long writerId, long bookId) {
        return orderDao.create(buyerId, writerId, bookId, OrderStatus.WAITING_CONTACT);
    }

    @Override
    public Optional<Order> find(long buyerId, long writerId, long bookId) {
        return orderDao.find(buyerId, writerId, bookId);
    }

    @Override
    public Order updateToWaitingPayment(Order order) {
        return updateStatus(order, OrderStatus.WAITING_CONTACT, OrderStatus.WAITING_PAYMENT);
    }

    @Override
    public Order updateToWaitingForBook(Order order) {
        return updateStatus(order, OrderStatus.WAITING_PAYMENT, OrderStatus.WAITING_FOR_BOOK);
    }

    @Override
    public Order updateToCompleted(Order order) {
        return updateStatus(order, OrderStatus.WAITING_CONTACT, OrderStatus.COMPLETED);
    }

    private Order updateStatus(Order order, OrderStatus expectedStatus, OrderStatus nextStatus){
        if (order.getOrderStatus() != expectedStatus){
            throw new IllegalOrderStatusUpdateException();
        }

        orderDao.setStatus(order.getBuyerId(),order.getWriterId(), order.getBookId(), nextStatus);
        return new Order(order.getWriterId(), order.getBuyerId(), order.getBookId(), nextStatus);
    }
}

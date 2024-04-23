package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.OrderDao;
import ar.edu.itba.paw.interfaces.OrderService;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(final OrderDao orderDao){
        this.orderDao = orderDao;
    }

    @Override
    public void create(long buyerId, long writerId, long bookId) {
        orderDao.create(buyerId, writerId, bookId, OrderStatus.WAITING_CONTACT);
    }

    @Override
    public Optional<Order> find(long buyerId, long writerId, long bookId) {
        return orderDao.find(buyerId, writerId, bookId);
    }

    @Override
    public Order toNextStatus(Order order){
        OrderStatus newStatus = order.getOrderStatus().getNext();
        orderDao.setStatus(order.getBuyer().getId(), order.getWriter().getId(), order.getBook().getBookId(), newStatus);
        return new Order(order.getBuyer(), order.getWriter(), order.getBook(), newStatus);
    }

    @Override
    public List<Order> getAllReaderOrders(long readerId) {
        return orderDao.getAllReaderOrders(readerId);
    }

    @Override
    public List<Order> getAllWriterOrders(long writerId) {
        return orderDao.getAllWriterOrders(writerId);
    }

    @Override
    public List<Order> getAllNonCompleteReaderOrders(long readerId) {
        return orderDao.getAllNonCompleteReaderOrders(readerId);
    }

    @Override
    public List<Order> getAllNonCompleteWriterOrders(long writerId) {
        return orderDao.getAllNonCompleteWriterOrders(writerId);
    }
}

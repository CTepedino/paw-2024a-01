package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    private final BookService bs;
    private final UserService us;
    private final MailService ms;

    @Autowired
    public OrderServiceImpl(final OrderDao orderDao, UserService us, MailService ms, BookService bs){
        this.orderDao = orderDao;
        this.us = us;
        this.ms = ms;
        this.bs = bs;
    }

    @Transactional
    @Override
    public void create(long bookId) {
        User buyer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);

        orderDao.create(buyer.getUserId(), bookId, OrderStatus.WAITING_CONTACT);
        ms.sendOrderEmail(buyer, book);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean canCreateOrder(long bookId) {
        User buyer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);

        if (book.getWriter().getUserId() == buyer.getUserId()){
            return false;
        }
        if (orderDao.find(buyer.getUserId(), bookId).isPresent()){
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> find(long buyerId, long writerId, long bookId) {
        return orderDao.find(buyerId, bookId);
    }

    @Transactional
    @Override
    public Order toNextStatus(Order order){
        OrderStatus newStatus = order.getOrderStatus().getNext();
        orderDao.setStatus(order.getBuyer().getUserId(), order.getBook().getBookId(), newStatus);
        return new Order(order.getBuyer(), order.getBook(), newStatus, order.getDate());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> getAllReaderOrders(long readerId) {
        return orderDao.getAllReaderOrders(readerId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> getAllWriterOrders(long writerId) {
        return orderDao.getAllWriterOrders(writerId);
    }

}

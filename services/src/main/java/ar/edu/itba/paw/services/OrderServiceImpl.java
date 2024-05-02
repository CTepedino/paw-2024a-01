package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.OrderStatus;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.OrderAlreadyExistsException;
import ar.edu.itba.paw.models.exception.SameWriterAndBuyerException;
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

        orderDao.create(buyer.getUserId(), book.getWriter().getId(), bookId, OrderStatus.WAITING_CONTACT);
        ms.sendOrderEmail(buyer.getUserId(), bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean canCreateOrder(long bookId) {
        User buyer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);

        if (book.getWriter().getId() == buyer.getUserId()){
            return false;
        }
        if (orderDao.find(buyer.getUserId(), book.getWriter().getId(), bookId).isPresent()){
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> find(long buyerId, long writerId, long bookId) {
        return orderDao.find(buyerId, writerId, bookId);
    }

    @Transactional
    @Override
    public Order toNextStatus(Order order){
        OrderStatus newStatus = order.getOrderStatus().getNext();
        orderDao.setStatus(order.getBuyer().getId(), order.getWriter().getId(), order.getBook().getBookId(), newStatus);
        return new Order(order.getBuyer(), order.getWriter(), order.getBook(), newStatus);
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

    @Transactional(readOnly = true)
    @Override
    public List<Order> getAllNonCompleteReaderOrders(long readerId) {
        return orderDao.getAllNonCompleteReaderOrders(readerId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> getAllNonCompleteWriterOrders(long writerId) {
        return orderDao.getAllNonCompleteWriterOrders(writerId);
    }
}

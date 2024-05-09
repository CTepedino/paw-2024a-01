package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.dao.files.PaymentReceiptDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.*;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final PaymentReceiptDao paymentReceiptDao;

    private final BookService bs;
    private final UserService us;
    private final MailService ms;

    @Autowired
    public OrderServiceImpl(final OrderDao orderDao, final PaymentReceiptDao paymentReceiptDao, UserService us, MailService ms, BookService bs){
        this.orderDao = orderDao;
        this.paymentReceiptDao = paymentReceiptDao;
        this.us = us;
        this.ms = ms;
        this.bs = bs;
    }

    @Transactional
    @Override
    public void create(long bookId, MultipartFile receipt) {
        User buyer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);

        try {

            long orderId = orderDao.create(buyer.getUserId(), bookId, OrderStatus.WAITING_CONTACT);
            paymentReceiptDao.create(orderId, receipt.getBytes());
            ms.sendOrderEmail(buyer, book);;
        } catch (IOException e){
            throw new UnreadableFileException();
        }
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
    public Optional<Order> find(long buyerId, long bookId) {
        return orderDao.find(buyerId, bookId);
    }

    @Transactional
    @Override
    public Order toNextStatus(Order order){
        OrderStatus newStatus = order.getOrderStatus().getNext();
        orderDao.setStatus(order.getOrderId(), newStatus);
        return new Order(order.getOrderId(), order.getBuyer(), order.getBook(), newStatus, order.getDate());
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Order> getAllReaderOrders(long readerId, int pageNumber, int pageSize) {
        List<Order> orders =  orderDao.getAllReaderOrders(readerId, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getAllReaderOrdersSize(readerId));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Order> getAllWriterOrders(long writerId, int pageNumber, int pageSize) {
        List<Order> orders =  orderDao.getAllWriterOrders(writerId, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getAllWriterOrdersSize(writerId));
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentReceipt getReceipt(long id){
        return paymentReceiptDao.findById(id).orElseThrow(PdfNotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> findById(long orderId) {
        return orderDao.findById(orderId);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Order> searchReaderOrdersWithParams(long readerId,  String title, OrderStatus orderStatus, int pageNumber, int pageSize){
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Order> orders = orderDao.getReaderOrdersWithParams(readerId, title, orderStatus, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getAllReaderOrdersSize(readerId));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Order> searchWriterOrdersWithParams(long writerId,  String title, OrderStatus orderStatus, int pageNumber, int pageSize){
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Order> orders = orderDao.getWriterOrdersWithParams(writerId, title, orderStatus, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getAllWriterOrdersSize(writerId));
    }
}

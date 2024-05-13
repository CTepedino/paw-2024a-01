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
            long orderId = orderDao.create(buyer.getUserId(), bookId, OrderStatus.WAITING_APPROVAL);
            paymentReceiptDao.create(orderId, receipt.getBytes());
            ms.sendOrderEmail(buyer, book);;
        } catch (IOException e){
            throw new UnreadableFileException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public boolean canCreateOrder(long bookId) {
        if (!us.isLoggedIn()){
            return false;
        }

        User buyer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);

        if (book.getWriter().getUserId() == buyer.getUserId() || book.isPaused()){
            return false;
        }
        return orderDao.find(buyer.getUserId(), bookId).isEmpty();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> find(long buyerId, long bookId) {
        return orderDao.find(buyerId, bookId);
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
    public PaginatedContent<Order> getReaderOrders(long readerId, String title, OrderStatus orderStatus,int pageNumber, int pageSize){
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Order> orders = orderDao.getReaderOrders(readerId, title, orderStatus,(pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getReaderOrdersSize(readerId, title, orderStatus));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Order> getWriterOrders(long writerId,  String title, OrderStatus orderStatus, int pageNumber, int pageSize){
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Order> orders = orderDao.getWriterOrders(writerId, title, orderStatus,(pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getWriterOrdersSize(writerId, title, orderStatus));
    }

    private void sendReceipt(Order order, MultipartFile receipt, OrderStatus fromStatus) {
        if (receipt == null){
            throw new InvalidOrderUpdateException();
        }
        orderDao.update(order.getOrderId(), OrderStatus.WAITING_APPROVAL);
        try {
            paymentReceiptDao.createOrUpdate(order.getOrderId(), receipt.getBytes());
        } catch (IOException e){
            throw new UnreadableFileException();
        }
        if (fromStatus.equals(OrderStatus.REJECTED_PAYMENT)){
            ms.sendReceiptReuploadedEmail(order);
        } else {
            ms.sendReceiptUploadedEmail(order);
        }
    }

    private void acceptOrReject(Order order, Boolean approved){
        if (approved == null){
            throw new InvalidOrderUpdateException();
        }
        if (approved) {
            orderDao.update(order.getOrderId(), OrderStatus.COMPLETED);
            ms.sendReceiptApprovedEmail(order);
        } else {
            orderDao.update(order.getOrderId(), OrderStatus.REJECTED_PAYMENT);
            ms.sendReceiptDeniedEmail(order);
        }
    }

    @Transactional
    @Override
    public void updateOrderWriterSide(long orderId, Boolean approved){
        Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (order.getOrderStatus().equals(OrderStatus.WAITING_APPROVAL)) {
            acceptOrReject(order, approved);
        } else {
            throw new InvalidOrderUpdateException();
        }
    }

    @Transactional
    @Override
    public void updateOrderBuyerSide(long orderId, MultipartFile receipt){
        Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

        switch (order.getOrderStatus()){
            case WAITING_PAYMENT, REJECTED_PAYMENT -> sendReceipt(order, receipt, order.getOrderStatus());
            case WAITING_CONTACT, COMPLETED, WAITING_APPROVAL -> throw new InvalidOrderUpdateException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public boolean loggedUserOwnsBook(long bookId){
        if (us.isLoggedIn()){
            return orderDao.ownsBook(bookId, us.getLoggedUser().get().getEmail());
        }
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasBookFileAccess(long bookId, String email) {
        return orderDao.ownsBook(bookId, email) || bs.findById(bookId).orElseThrow(BookNotFoundException::new).getWriter().getEmail().equals(email);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean canAdvanceOrder(long orderId, String email) {
        Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);
        return !order.getBook().isPaused() &&
            (order.getWriter().getEmail().equals(email) && order.getOrderStatus().getWriterCanAdvance()) ||
            (order.getBuyer().getEmail().equals(email) && order.getOrderStatus().getReaderCanAdvance());
    }

}

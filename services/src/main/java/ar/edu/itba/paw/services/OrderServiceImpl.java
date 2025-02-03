package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    private final BookService bs;
    private final UserService us;
    private final MailService ms;

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(final OrderDao orderDao, UserService us, MailService ms, BookService bs){
        this.orderDao = orderDao;
        this.us = us;
        this.ms = ms;
        this.bs = bs;
    }

    @Transactional
    @Override
    public Long create(long bookId) {
        User buyer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        if (book.getWriter().getUserId() == buyer.getUserId()){
            throw new IllegalOrderException();
        }
        if (orderDao.find(buyer.getUserId(), bookId).isPresent()){
            throw new OrderAlreadyExistsException();
        }
        Order order = orderDao.create(buyer, book, OrderStatus.WAITING_PAYMENT, LocalDateTime.now(), book.getDeal()==null? book.getPrice(): book.getDeal().getPrice());
        bs.removeFromWishlist(buyer.getUserId(), bookId);
        bs.checkBookSalesCategory(book);
        LOGGER.atDebug().setMessage("Created order for bookId: {}").addArgument(bookId).log();
        return order.getOrderId();
    }

    @Transactional
    @Override
    public Order create(long bookId, byte[] receipt, String receiptMimeType) {
        User buyer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        Order order = orderDao.create(buyer, book, OrderStatus.WAITING_APPROVAL, LocalDateTime.now(), book.getDeal()==null? book.getPrice(): book.getDeal().getPrice());
        bs.removeFromWishlist(buyer.getUserId(), bookId);
        bs.checkBookSalesCategory(book);
        us.checkWriterCategory(book.getWriter());

        orderDao.createOrUpdatePaymentReceipt(order, receipt, receiptMimeType);

        LOGGER.atDebug().setMessage("Created order for bookId: {}").addArgument(bookId).log();
        ms.sendReceiptUploadedEmail(order);
        return order;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean canCreateOrder(long bookId) {
        if (!us.isLoggedIn()){
            return false;
        }

        User buyer = us.getLoggedUser().get();
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);

        if (book.getWriter().getUserId() == buyer.getUserId() || book.isPaused()){
            return false;
        }
        return orderDao.find(buyer.getUserId(), bookId).isEmpty();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsOrder(long bookId) {
        if (!us.isLoggedIn()){
            return false;
        }

        User buyer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        return orderDao.find(buyer.getUserId(), bookId).isPresent();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> find(long buyerId, long bookId) {
        return orderDao.find(buyerId, bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentReceipt getReceipt(long id){
        return orderDao.findById(id).orElseThrow(OrderNotFoundException::new).getPaymentReceipt();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> findById(long orderId) {
        return orderDao.findById(orderId);
    }

    private void sendReceipt(Order order, byte[] receipt, String receiptMimeType, OrderStatus fromStatus) {
        if (receipt == null){
            LOGGER.atWarn().setMessage("Failed to send upload receipt for orderId: {} - Error Message: No receipt provided").addArgument(order.getOrderId()).log();
            throw new InvalidOrderUpdateException();
        }
        orderDao.update(order, OrderStatus.WAITING_APPROVAL, order.getDate());

        orderDao.createOrUpdatePaymentReceipt(order, receipt, receiptMimeType);
        LOGGER.atDebug().setMessage("Uploaded receipt for orderId: {}").addArgument(order.getOrderId()).log();
        if (fromStatus.equals(OrderStatus.REJECTED_PAYMENT)){
            ms.sendReceiptReuploadedEmail(order);
        } else {
            ms.sendReceiptUploadedEmail(order);
        }
    }

    private void acceptOrReject(Order order, String rejectedReason){
        if (rejectedReason == null) {
            orderDao.update(order, OrderStatus.COMPLETED, order.getDate());
            ms.sendReceiptApprovedEmail(order);
        } else {
            orderDao.update(order, OrderStatus.REJECTED_PAYMENT, order.getDate(), rejectedReason);
            ms.sendReceiptDeniedEmail(order);
        }
        LOGGER.atDebug().setMessage("Successfully updated order status for orderId: {}").addArgument(order.getOrderId()).log();
    }

    @Transactional
    @Override
    public void updateOrderWriterSide(long orderId, String rejectedReason){
        Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (order.getOrderStatus().equals(OrderStatus.WAITING_APPROVAL)) {
            acceptOrReject(order, rejectedReason);
        } else {
            LOGGER.atWarn().setMessage("Failed to update order status from writer side for orderId: {}").addArgument(orderId).log();
            throw new InvalidOrderUpdateException();
        }
    }

    @Transactional
    @Override
    public void updateOrderBuyerSide(long orderId, byte[] receipt, String receiptMimeType){
        Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

        switch (order.getOrderStatus()){
            case WAITING_PAYMENT, REJECTED_PAYMENT -> sendReceipt(order, receipt, receiptMimeType, order.getOrderStatus());
            case COMPLETED, WAITING_APPROVAL -> {
                LOGGER.atWarn().setMessage("Failed to update order status from buyer side for orderId: {}").addArgument(orderId).log();
                throw new InvalidOrderUpdateException();
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public boolean ownsBook(long userId, long bookId){
        return orderDao.find(userId, bookId)
                .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED)
                .isPresent();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsOrder(long userId, long bookId){
        return orderDao.find(userId, bookId)
                .isPresent();
    }


    @Transactional(readOnly = true)
    @Override
    public boolean canAdvanceOrder(long orderId, String email) {
        Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);
        return !order.getBook().isPaused() &&
            (order.getWriter().getEmail().equals(email) && order.getOrderStatus().canWriterAdvance()) ||
            (order.getBuyer().getEmail().equals(email) && order.getOrderStatus().canReaderAdvance());
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Order> searchOrders(Long bookId, Long writerId, Long readerId, String title, OrderStatus orderStatus, int pageNumber, int pageSize) {
        List<Order> orders = orderDao.getAllOrders(bookId, writerId, readerId, title, orderStatus, (pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getAllOrdersSize(bookId, writerId, readerId, title, orderStatus));
    }
}
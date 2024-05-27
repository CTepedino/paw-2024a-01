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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final PaymentReceiptDao paymentReceiptDao;

    private final BookService bs;
    private final UserService us;
    private final MailService ms;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

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
        long orderId = orderDao.create(buyer.getUserId(), bookId, OrderStatus.WAITING_APPROVAL);
        try {
            paymentReceiptDao.create(orderId, receipt.getBytes(), receipt.getContentType());
        } catch (IOException e){
            LOGGER.atWarn().setMessage("Failed to create order for bookId: {} - Error Message: {}").addArgument(bookId).addArgument(e.getMessage()).log();
            throw new UnreadableFileException();
        }
        LOGGER.atDebug().setMessage("Created order for bookId: {}").addArgument(bookId).log();
        ms.sendReceiptUploadedEmail(findById(orderId).orElseThrow(OrderNotFoundException::new));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean canCreateOrder(long bookId) {
/*        if (!us.isLoggedIn()){
            return false;
        }

        User buyer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);

        if (book.getWriter().getUserId() == buyer.getUserId() || book.isPaused()){
            return false;
        }
        return orderDao.find(buyer.getUserId(), bookId).isEmpty();*/
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsOrder(long bookId) {
/*        if (!us.isLoggedIn()){
            return false;
        }

        User buyer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        return orderDao.find(buyer.getUserId(), bookId).isPresent();*/
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> find(long buyerId, long bookId) {
        //return orderDao.find(buyerId, bookId);
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentReceipt getReceipt(long id){
        //return paymentReceiptDao.findById(id).orElseThrow(PdfNotFoundException::new);
        return new PaymentReceipt(1, new byte[]{},"");
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> findById(long orderId) {
        //return orderDao.findById(orderId);
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Order> getReaderOrders(long readerId, String title, OrderStatus orderStatus,int pageNumber, int pageSize){
/*        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Order> orders = orderDao.getReaderOrders(readerId, title, orderStatus,(pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getReaderOrdersSize(readerId, title, orderStatus));*/
        return new PaginatedContent<>(Collections.emptyList(), pageNumber, pageSize, 0);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Order> getWriterOrders(long writerId,  String title, OrderStatus orderStatus, int pageNumber, int pageSize){
/*        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Order> orders = orderDao.getWriterOrders(writerId, title, orderStatus,(pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getWriterOrdersSize(writerId, title, orderStatus));*/
        return new PaginatedContent<>(Collections.emptyList(), pageNumber, pageSize, 0);
    }

    private void sendReceipt(Order order, MultipartFile receipt, OrderStatus fromStatus) {
/*        if (receipt == null){
            LOGGER.atWarn().setMessage("Failed to send upload receipt for orderId: {} - Error Message: No receipt provided").addArgument(order.getOrderId()).log();
            throw new InvalidOrderUpdateException();
        }
        orderDao.update(order.getOrderId(), OrderStatus.WAITING_APPROVAL);
        try {
            paymentReceiptDao.createOrUpdate(order.getOrderId(), receipt.getBytes(), receipt.getContentType());
            LOGGER.atDebug().setMessage("Uploaded receipt for orderId: {}").addArgument(order.getOrderId()).log();
        } catch (IOException e){
            LOGGER.atWarn().setMessage("Failed to upload receipt for orderId: {} - Error Message: {}").addArgument(order.getOrderId()).addArgument(e.getMessage()).log();
            throw new UnreadableFileException();
        }
        if (fromStatus.equals(OrderStatus.REJECTED_PAYMENT)){
            ms.sendReceiptReuploadedEmail(order);
        } else {
            ms.sendReceiptUploadedEmail(order);
        }*/
    }

    private void acceptOrReject(Order order, boolean approved){
/*        if (approved) {
            orderDao.update(order.getOrderId(), OrderStatus.COMPLETED);
            ms.sendReceiptApprovedEmail(order);
        } else {
            orderDao.update(order.getOrderId(), OrderStatus.REJECTED_PAYMENT);
            ms.sendReceiptDeniedEmail(order);
        }
        LOGGER.atDebug().setMessage("Successfully updated order status for orderId: {}").addArgument(order.getOrderId()).log();*/
    }

    @Transactional
    @Override
    public void updateOrderWriterSide(long orderId, boolean approved){
/*        Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (order.getOrderStatus().equals(OrderStatus.WAITING_APPROVAL)) {
            acceptOrReject(order, approved);
        } else {
            LOGGER.atWarn().setMessage("Failed to update order status for orderId: {}").addArgument(orderId).log();
            throw new InvalidOrderUpdateException();
        }*/
    }

    @Transactional
    @Override
    public void updateOrderBuyerSide(long orderId, MultipartFile receipt){
/*        Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

        switch (order.getOrderStatus()){
            case WAITING_PAYMENT, REJECTED_PAYMENT -> sendReceipt(order, receipt, order.getOrderStatus());
            case WAITING_CONTACT, COMPLETED, WAITING_APPROVAL -> {
                LOGGER.atWarn().setMessage("Failed to update order status for orderId: {}").addArgument(orderId).log();
                throw new InvalidOrderUpdateException();
            }
        }*/
    }

    @Transactional(readOnly = true)
    @Override
    public boolean loggedUserOwnsBook(long bookId){
/*        if (us.isLoggedIn()){
            return orderDao.ownsBook(bookId, us.getLoggedUser().get().getEmail());
        }
        return false;*/
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasBookFileAccess(long bookId, String email) {
        //return orderDao.ownsBook(bookId, email) || bs.findById(bookId).orElseThrow(BookNotFoundException::new).getWriter().getEmail().equals(email);
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean canAdvanceOrder(long orderId, String email) {
/*        Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);
        return !order.getBook().isPaused() &&
            (order.getWriter().getEmail().equals(email) && order.getOrderStatus().getWriterCanAdvance()) ||
            (order.getBuyer().getEmail().equals(email) && order.getOrderStatus().getReaderCanAdvance());*/
        return false;
    }

    @Transactional
    @Override
    public void recommendBook(long orderId, boolean isRecommended){
        //orderDao.recommendBook(orderId, isRecommended);
    }

}

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
import ar.edu.itba.paw.models.orders.OrderOrderBy;
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
    public PaginatedContent<Order> getReaderOrders(long readerId, String title, OrderStatus orderStatus, OrderOrderBy orderBy, int pageNumber, int pageSize){
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Order> orders = orderDao.getReaderOrders(readerId, title, orderStatus, orderBy,(pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getReaderOrdersSize(readerId, title, orderStatus));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Order> getWriterOrders(long writerId,  String title, OrderStatus orderStatus, OrderOrderBy orderBy,int pageNumber, int pageSize){
        if (pageNumber < 1){
            throw new InvalidPageException();
        }
        List<Order> orders = orderDao.getWriterOrders(writerId, title, orderStatus, orderBy,(pageNumber-1)*pageSize, pageSize);
        return new PaginatedContent<>(orders, pageNumber, pageSize, orderDao.getWriterOrdersSize(writerId, title, orderStatus));
    }

    @Transactional
    @Override
    public void atCbuAdded(long writerId) {
        orderDao.updateAllWriterOrders(writerId, OrderStatus.WAITING_CONTACT, OrderStatus.WAITING_PAYMENT);
    }


    private void sendReceipt(long orderId, MultipartFile receipt) {
        if (receipt == null){
            throw new InvalidOrderUpdateException();
        }
        orderDao.update(orderId, OrderStatus.WAITING_APPROVAL);
        try {
            paymentReceiptDao.createOrUpdate(orderId, receipt.getBytes());
        } catch (IOException e){
            throw new UnreadableFileException();
        }
        //ms.sendReceiptUploadedEmail();
    }

    private void acceptOrReject(long orderId, Boolean approved){
        if (approved == null){
            throw new InvalidOrderUpdateException();
        }
        if (approved) {
            orderDao.update(orderId, OrderStatus.COMPLETED);
            // ms.sendReceiptApprovedEmail();
        } else {
            orderDao.update(orderId, OrderStatus.WAITING_PAYMENT);
            // ms.sendReceiptDeniedEmail();
        }
    }

    @Transactional
    @Override
    public void updateOrder(long orderId, MultipartFile receipt, Boolean approved){
        Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

        switch (order.getOrderStatus()){
            case WAITING_CONTACT, COMPLETED -> throw new InvalidOrderUpdateException();
            case WAITING_PAYMENT -> sendReceipt(orderId, receipt);
            case WAITING_APPROVAL -> acceptOrReject(orderId, approved);
        }
    }
}

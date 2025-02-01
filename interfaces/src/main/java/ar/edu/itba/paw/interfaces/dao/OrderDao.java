package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderDao {

    List<Order> getAllOrders(Long bookId, Long writerId, Long readerId, String title, OrderStatus orderStatus, int offset, int limit);
    long getAllOrdersSize(Long bookId, Long writerId, Long readerId, String title, OrderStatus orderStatus);

    Optional<Order> find(long buyerId, long bookId);

    Optional<Order> findById(long orderId);

    Order create(User user, Book book, OrderStatus orderStatus, LocalDateTime date, BigDecimal price);

    void update(Order order, OrderStatus orderStatus, LocalDateTime date);

    void update(Order order, OrderStatus orderStatus, LocalDateTime date, String rejectedReason);

    PaymentReceipt createOrUpdatePaymentReceipt(Order order, byte[] paymentReceipt, String type);

    List<Order> getReaderOrders(long readerId, String title, OrderStatus orderStatus, int offset, int limit);
    long getReaderOrdersSize(long readerId, String title, OrderStatus orderStatus);

    List<Order> getWriterOrders(long writerId, String title, OrderStatus orderStatus, int offset, int limit);
    long getWriterOrdersSize(long writerId, String title, OrderStatus orderStatus);

    boolean ownsBook(long bookId, String email);


    long getTotalOrdersForBook(long bookId);

    BigDecimal getTotalSales(long writerId);

    BigDecimal getTotalSalesForBook(long bookId);

    BigDecimal getWriterTotalSalesPerMonth(long writerId, int year, int month);
    long getWriterTotalOrdersPerMonth(long writerId, int year, int month);
    BigDecimal getBookTotalSalesPerMonth(long bookId, int year, int month);
    long getBookTotalOrdersPerMonth(long bookId, int year, int month);

    long getBooksByWriterOrderedSize(long writerId);
    long getBooksByWriterOrderedSize(long writerId, int year, int month);
}

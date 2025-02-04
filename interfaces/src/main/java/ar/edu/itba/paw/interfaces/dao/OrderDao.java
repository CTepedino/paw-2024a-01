package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderDao {

    Order create(User user, Book book, OrderStatus orderStatus, LocalDateTime date, BigDecimal price);

    void update(Order order, OrderStatus orderStatus, LocalDateTime date);
    void update(Order order, OrderStatus orderStatus, LocalDateTime date, String rejectedReason);

    void createOrUpdatePaymentReceipt(Order order, byte[] paymentReceipt, String type);

    List<Order> getAllOrders(Long bookId, Long writerId, Long readerId, String title, OrderStatus orderStatus, int offset, int limit);
    long getAllOrdersSize(Long bookId, Long writerId, Long readerId, String title, OrderStatus orderStatus);

    Optional<Order> find(long buyerId, long bookId);
    Optional<Order> findById(long orderId);

    BigDecimal getWriterTotalSalesPerMonth(long writerId, int year, int month);
    long getWriterTotalOrdersPerMonth(long writerId, int year, int month);
    BigDecimal getBookTotalSalesPerMonth(long bookId, int year, int month);
    long getBookTotalOrdersPerMonth(long bookId, int year, int month);

}

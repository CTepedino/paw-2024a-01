package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    PaginatedContent<Order> searchOrders(Long bookId, Long writerId, Long readerId, String title, OrderStatus orderStatus, int pageNumber, int pageSize);

    Long create(long bookId, long userId);

    Order create(long bookId, byte[] receipt, String receiptMimeType);

    boolean existsOrder(long bookId);

    boolean canCreateOrder(long bookId);

    Optional<Order> find(long buyerId, long bookId);
    Optional<Order> findById(long orderId);

    PaymentReceipt getReceipt(long id);

    boolean loggedUserOwnsBook(long bookId);

    boolean hasBookFileAccess(long bookId, String email);

    boolean canAdvanceOrder(long orderId, String email);

    void updateOrderWriterSide(long orderId, String rejectedReason);

    void updateOrderBuyerSide(long orderId, byte[] receipt, String receiptMimeType);

}

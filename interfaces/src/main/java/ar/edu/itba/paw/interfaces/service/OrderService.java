package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    void create(long bookId, MultipartFile receipt);

    boolean canCreateOrder(long bookId);

    Optional<Order> find(long buyerId, long bookId);
    Optional<Order> findById(long orderId);

    PaymentReceipt getReceipt(long id);

    PaginatedContent<Order> getReaderOrders(long readerId, String title, OrderStatus orderStatus,int pageNumber, int pageSize);

    PaginatedContent<Order> getWriterOrders(long writerId, String title, OrderStatus orderStatus,int pageNumber, int pageSize);


    boolean loggedUserOwnsBook(long bookId);

    boolean hasBookFileAccess(long bookId, String email);

    boolean canAdvanceOrder(long orderId, String email);

    void updateOrderWriterSide(long orderId, boolean approved);

    void updateOrderBuyerSide(long orderId, MultipartFile receipt);

}

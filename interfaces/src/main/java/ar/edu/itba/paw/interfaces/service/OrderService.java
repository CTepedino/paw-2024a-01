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


/*    Order receiptSent(Order order);
    Order acceptReceipt(Order order);
    Order rejectReceipt(Order order);*/



    Order toNextStatus(Order order);

    PaginatedContent<Order> getAllReaderOrders(long readerId, int pageNumber, int pageSize);

    PaginatedContent<Order> getAllWriterOrders(long writerId, int pageNumber, int pageSize);




    PaginatedContent<Order> searchReaderOrdersWithParams(long readerId, String title, OrderStatus orderStatus, int pageNumber, int pageSize);

    PaginatedContent<Order> searchWriterOrdersWithParams(long writerId, String title, OrderStatus orderStatus, int pageNumber, int pageSize);
}

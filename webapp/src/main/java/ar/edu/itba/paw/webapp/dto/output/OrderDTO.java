package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.function.Function;

public class OrderDTO {

    private long orderId;
    private long buyerId;
    private long bookId;
    private long sellerId;
    private OrderStatus status;
    private LocalDateTime date;
    private String rejectedReason;
    private BigDecimal price;

    private URI self;
    private URI book;
    private URI buyer;
    private URI seller;
    private URI receipt;

    public static Function<Order, OrderDTO> mapper(UriInfo uriInfo){
        return o -> fromOrder(uriInfo, o);
    }

    public static OrderDTO fromOrder(UriInfo uriInfo, Order o){
        OrderDTO dto = new OrderDTO();

        dto.orderId = o.getOrderId();
        dto.buyerId = o.getBuyer().getUserId();
        dto.bookId = o.getBook().getBookId();
        dto.sellerId = o.getBook().getWriter().getUserId();
        dto.status = o.getOrderStatus();
        dto.date = o.getDate();
        dto.rejectedReason = o.getRejectedReason();
        dto.price = o.getPrice();

        dto.self = uriInfo.getBaseUriBuilder().path("orders").path(String.valueOf(dto.orderId)).build();
        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(dto.bookId)).build();
        dto.buyer = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dto.buyerId)).build();
        dto.seller = uriInfo.getBaseUriBuilder().path("seller").path(String.valueOf(dto.sellerId)).build();
        dto.receipt = uriInfo.getBaseUriBuilder().path("orders").path(String.valueOf(dto.orderId)).path("receipt").build();

        return dto;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(long buyerId) {
        this.buyerId = buyerId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getBook() {
        return book;
    }

    public void setBook(URI book) {
        this.book = book;
    }

    public URI getBuyer() {
        return buyer;
    }

    public void setBuyer(URI buyer) {
        this.buyer = buyer;
    }

    public URI getSeller() {
        return seller;
    }

    public void setSeller(URI seller) {
        this.seller = seller;
    }

    public URI getReceipt() {
        return receipt;
    }

    public void setReceipt(URI receipt) {
        this.receipt = receipt;
    }
}

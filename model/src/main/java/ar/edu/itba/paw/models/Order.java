package ar.edu.itba.paw.models;

public class Order {
    private final long writerId;
    private final long buyerId;
    private final long bookId;
    private final OrderStatus orderStatus;

    public Order(long writerId, long buyerId, long bookId, OrderStatus orderStatus) {
        this.writerId = writerId;
        this.buyerId = buyerId;
        this.bookId = bookId;
        this.orderStatus = orderStatus;
    }

    public long getWriterId() {
        return writerId;
    }

    public long getBuyerId() {
        return buyerId;
    }

    public long getBookId() {
        return bookId;
    }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}

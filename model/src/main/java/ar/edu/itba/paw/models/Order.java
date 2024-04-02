package ar.edu.itba.paw.models;

import java.time.LocalDate;

public class Order {

    private final long orderId;
    private final long bookId;
    private final long buyerId;
    private final PaymentMethod paymentMethod;
    private final LocalDate date;


    public Order(long orderId, long bookId, long buyerId, PaymentMethod paymentMethod, LocalDate date){
        this.orderId = orderId;
        this.bookId = bookId;
        this.buyerId = buyerId;
        this.paymentMethod = paymentMethod;
        this.date = date;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getBookId() {
        return bookId;
    }

    public long getBuyerId() {
        return buyerId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDate getDate() {
        return date;
    }
}

package ar.edu.itba.paw.models;

public class Order {
    private final PublicUserInformation writer;
    private final PublicUserInformation buyer;
    private final Book book;
    private final OrderStatus orderStatus;


    public Order(PublicUserInformation writer, PublicUserInformation buyer, Book book, OrderStatus orderStatus) {
        this.writer = writer;
        this.buyer = buyer;
        this.book = book;
        this.orderStatus = orderStatus;
    }

    public PublicUserInformation getWriter() {
        return writer;
    }

    public PublicUserInformation getBuyer() {
        return buyer;
    }

    public Book getBook() {
        return book;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}

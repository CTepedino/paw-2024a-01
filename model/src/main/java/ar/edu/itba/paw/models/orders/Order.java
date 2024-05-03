package ar.edu.itba.paw.models.orders;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.users.User;

public class Order {
    private final User buyer;
    private final Book book;
    private final OrderStatus orderStatus;


    public Order(User buyer, Book book, OrderStatus orderStatus) {
        this.buyer = buyer;
        this.book = book;
        this.orderStatus = orderStatus;
    }

    public User getWriter(){
        return book.getWriter();
    }

    public User getBuyer() {
        return buyer;
    }

    public Book getBook() {
        return book;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}

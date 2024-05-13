package ar.edu.itba.paw.models.orders;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.users.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Order {
    private final long orderId;
    private final User buyer;
    private final Book book;
    private final OrderStatus orderStatus;
    private final LocalDateTime date;


    public Order(long orderId, User buyer, Book book, OrderStatus orderStatus, LocalDateTime date) {
        this.orderId = orderId;
        this.buyer = buyer;
        this.book = book;
        this.orderStatus = orderStatus;
        this.date = date;
    }

    public long getOrderId() {
        return orderId;
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

    public LocalDateTime getDate() {
        return date;
    }

    public String getFormattedDate(Locale locale) {
        return date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(locale));
    }
}

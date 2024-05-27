package ar.edu.itba.paw.models.orders;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.users.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_order_id_seq")
    @SequenceGenerator(sequenceName = "orders_order_id_seq", name = "orders_order_id_seq", allocationSize = 1)
    @Column(name = "order_id")
    private Long orderId;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "buyer_id", referencedColumnName = "user_id", nullable = false)
    private User buyer;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable = false)
    private Book book;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    private LocalDateTime date;

    @Column(name = "is_public")
    private boolean isPublic;

    protected Order(){}

    public Order(User buyer, Book book, OrderStatus orderStatus, LocalDateTime date, boolean isPublic) {
        this.buyer = buyer;
        this.book = book;
        this.orderStatus = orderStatus;
        this.date = date;
        this.isPublic=isPublic;
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

    public boolean getIsPublic() {
        return isPublic;
    }

    public String getFormattedDate(Locale locale) {
        return date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(locale));
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}

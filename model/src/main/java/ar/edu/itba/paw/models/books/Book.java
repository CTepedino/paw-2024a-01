package ar.edu.itba.paw.models.books;

import ar.edu.itba.paw.models.users.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_book_id_seq")
    @SequenceGenerator(sequenceName = "books_book_id_seq", name = "books_book_id_seq", allocationSize = 1)
    @Column(name = "book_id")
    private Long bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookGenre genre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "page_count", nullable = false)
    private int pageCount;

    @Column(name = "suggested_age", nullable = false)
    private int suggestedAge;

    @Column(name = "published_date")
    private LocalDate publishDate;

    @Column(name = "is_paused")
    private boolean isPaused;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "writer_id", referencedColumnName = "user_id")
    private User writer;


    protected Book(){}

    public Book(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, LocalDate publishDate, User writer, boolean isPaused) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.price = price;
        this.pageCount = pageCount;
        this.suggestedAge = suggestedAge;
        this.publishDate = publishDate;
        this.writer = writer;
        this.isPaused = isPaused;
    }

    public Book(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, LocalDate publishDate, User writer, boolean isPaused) {
        this.bookId = bookId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.price = price;
        this.pageCount = pageCount;
        this.suggestedAge = suggestedAge;
        this.publishDate = publishDate;
        this.writer = writer;
        this.isPaused = isPaused;
    }




    public String getFormattedPrice(){
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("es").setRegion("AR").build());
        currencyFormatter.setMaximumFractionDigits(0);
        return currencyFormatter.format(price);
    }

    public long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BookGenre getGenre() {
        return genre;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getSuggestedAge() {
        return suggestedAge;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isPaused() {return isPaused;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(BookGenre genre) {
        this.genre = genre;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setSuggestedAge(int suggestedAge) {
        this.suggestedAge = suggestedAge;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}

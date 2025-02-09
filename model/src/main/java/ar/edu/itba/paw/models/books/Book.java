package ar.edu.itba.paw.models.books;

import ar.edu.itba.paw.models.deals.Deal;
import ar.edu.itba.paw.models.files.BookFile;
import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.CoverImage;
import ar.edu.itba.paw.models.users.User;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "writer_id", referencedColumnName = "user_id")
    private User writer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookFile bookFile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private CoverImage coverImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookPreview preview;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Deal deal;

    @Column(name = "sales_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookSalesCategory salesCategory;

    @Formula("(SELECT AVG(r.rating) FROM reviews r WHERE r.book_id = book_id)")
    private Double averageRating;

    @Formula("(SELECT COALESCE(COUNT(DISTINCT o.order_id), 0) FROM orders o WHERE o.status = 'COMPLETED' AND o.book_id = book_id)")
    private long orderCount;

    @Formula("(SELECT COALESCE(SUM(o.price), 0) FROM orders o WHERE o.status = 'COMPLETED' AND o.book_id = book_id)")
    private double salesTotal;

    Book(){}

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
        this.salesCategory = BookSalesCategory.DEFAULT;
    }

    public Book(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, LocalDate publishDate, User writer, boolean isPaused) {
        this(title, description, genre, price, pageCount, suggestedAge, publishDate, writer, isPaused);
        this.bookId = bookId;
    }

    public String getFormattedPrice(){
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("es").setRegion("AR").build());
        currencyFormatter.setMaximumFractionDigits(0);
        return currencyFormatter.format(price);
    }


    public String getPercentage(){
        if(deal == null ){
            return null;
        }

        BigDecimal change = deal.getPrice().subtract(price);
        BigDecimal percentageChange = change.divide(price, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        return String.format("%+d%%", percentageChange.intValue());
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

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public BookFile getBookFile(){
        return bookFile;
    }

    public CoverImage getCoverImage() {
        return coverImage;
    }

    public BookPreview getPreview() {
        return preview;
    }

    public void setBookFile(BookFile bookFile) {
        this.bookFile = bookFile;
    }

    public void setCoverImage(CoverImage coverImage) {
        this.coverImage = coverImage;
    }

    public void setPreview(BookPreview preview) {
        this.preview = preview;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public BookSalesCategory getSalesCategory() {
        return salesCategory;
    }

    public void setSalesCategory(BookSalesCategory salesCategory) {
        this.salesCategory = salesCategory;
    }


    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public int getAverageRating(){
         return averageRating!=null? (int)Math.ceil(averageRating):0;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

    public double getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(double salesTotal) {
        this.salesTotal = salesTotal;
    }
}

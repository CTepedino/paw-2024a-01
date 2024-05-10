package ar.edu.itba.paw.models.books;

import ar.edu.itba.paw.models.users.User;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class Book {
    private final long bookId;
    private final String title;
    private final String description;
    private final BookGenre genre;
    private final BigDecimal price;
    private final int pageCount;
    private final int suggestedAge;
    private final LocalDate publishDate;

    private final User writer;

    public String getFormattedPrice(){
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("es").setRegion("AR").build());
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

    public Book(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, LocalDate publishDate, User writer) {
        this.bookId = bookId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.price = price;
        this.pageCount = pageCount;
        this.suggestedAge = suggestedAge;
        this.publishDate = publishDate;
        this.writer = writer;
    }
}

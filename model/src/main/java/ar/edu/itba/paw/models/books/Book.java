package ar.edu.itba.paw.models.books;

import ar.edu.itba.paw.models.users.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Book {
    private final long bookId;
    private final String title;
    private final String description;
    private final BookGenre genre;
    private final BigDecimal price;
    private final int pageCount;
    private final int suggestedAge;
    private final LocalDate publishDate;

    private final long previewId;
    private final long coverId;
    private final Long bookFileId;

    private final User writer;

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

    public long getPreviewId() {
        return previewId;
    }

    public long getCoverId() {
        return coverId;
    }

    public Long getBookFileId() {
        return bookFileId;
    }

    public User getWriter() {
        return writer;
    }

    public Book(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, LocalDate publishDate, long previewId, long coverId, long bookFileId, User writer) {
        this.bookId = bookId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.price = price;
        this.pageCount = pageCount;
        this.suggestedAge = suggestedAge;
        this.publishDate = publishDate;
        this.previewId = previewId;
        this.coverId = coverId;
        this.bookFileId = bookFileId;
        this.writer = writer;
    }
}

package ar.edu.itba.paw.models.books;

import ar.edu.itba.paw.models.users.User;

import java.time.LocalDate;

public class Book {
    private final long bookId;
    private final String title;
    private final String description;
    private final BookGenre genre;
    private final double price;
    private final int pageCount;
    private final int suggestedAge;
    private final LocalDate publishDate;

    private final long previewId;
    private final long coverId;

    private final User writer;

    public Book(long bookId, String title, String description, BookGenre genre, double price, int pageCount, int suggestedAge, LocalDate publishDate, long previewId, long coverId, User writer) {
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
        this.writer = writer;
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

    public double getPrice() {
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

    public long getPreviewId() {
        return previewId;
    }

    public long getCoverId() {
        return coverId;
    }
}

package ar.edu.itba.paw.models;

import java.sql.Date;

public class Book {
    private final long bookId;
    private final String title;
    private final String description;
    private final BookGenre genre;
    private final double price;
    private final int pageCount;
    private final long previewPdfId;
    private final long imageId;
    private final int suggestedAge;
    private final Date publishDate;
    private final long writerId;


    public Book(long bookId, String title, String description, BookGenre genre, double price, int pageCount, long previewPdfId, long imageId, int suggestedAge, Date publishDate, long writerId) {
        this.bookId = bookId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.price = price;
        this.pageCount = pageCount;
        this.previewPdfId = previewPdfId;
        this.imageId = imageId;
        this.suggestedAge = suggestedAge;
        this.publishDate = publishDate;
        this.writerId = writerId;
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

    public long getPreviewPdfId() {
        return previewPdfId;
    }

    public long getImageId() {
        return imageId;
    }

    public int getSuggestedAge() {
        return suggestedAge;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public long getWriterId() {
        return writerId;
    }
}

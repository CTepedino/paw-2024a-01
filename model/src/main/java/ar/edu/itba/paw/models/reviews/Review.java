package ar.edu.itba.paw.models.reviews;

import ar.edu.itba.paw.models.users.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Review {
    private final long bookId;
    private final User reviewer;
    private final int rating;
    private final String review;
    private final LocalDateTime date;

    public Review(long bookId, User reviewer, int rating, String review, LocalDateTime date) {
        this.bookId = bookId;
        this.reviewer = reviewer;
        this.rating = rating;
        this.review = review;
        this.date = date;
    }

    public long getBookId() {
        return bookId;
    }

    public User getReviewer() {
        return reviewer;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getFormattedDate(Locale locale) {
        return date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(locale));
    }
}

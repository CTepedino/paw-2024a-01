package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.users.User;

import java.time.LocalDateTime;

public class Review {
    private final long bookId;
    private final User reviewer;
    private final int rating;
    private final String review;
    private final LocalDateTime time;

    public Review(long bookId, User reviewer, int rating, String review, LocalDateTime time) {
        this.bookId = bookId;
        this.reviewer = reviewer;
        this.rating = rating;
        this.review = review;
        this.time = time;
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

    public LocalDateTime getTime() {
        return time;
    }
}

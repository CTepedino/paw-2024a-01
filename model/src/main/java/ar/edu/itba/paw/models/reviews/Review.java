package ar.edu.itba.paw.models.reviews;

import ar.edu.itba.paw.models.users.User;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"book_id", "reviewer_id"}))
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_review_id_seq")
    @SequenceGenerator(sequenceName = "reviews_review_id_seq", name = "reviews_review_id_seq", allocationSize = 1)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "book_id")
    private long bookId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewer_id", referencedColumnName = "user_id")
    private User reviewer;

    @Column
    private int rating;

    @Column
    private String review;

    @Column
    private LocalDateTime date;

    Review(){}

    public Review(long bookId, User reviewer, int rating, String review, LocalDateTime date) {
        this.bookId = bookId;
        this.reviewer = reviewer;
        this.rating = rating;
        this.review = review;
        this.date = date;
    }

    public long getReviewId() {
        return reviewId;
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

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}


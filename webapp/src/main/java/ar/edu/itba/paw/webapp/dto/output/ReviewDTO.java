package ar.edu.itba.paw.webapp.dto.output;


import ar.edu.itba.paw.models.reviews.Review;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.function.Function;

public class ReviewDTO {

    private long reviewerId;
    private int rating;
    private String review;
    private LocalDateTime date;

    private URI self;
    private URI book;
    private URI reviewer;

    public static Function<Review, ReviewDTO> mapper(UriInfo uriInfo){
        return r -> fromReview(uriInfo, r);
    }

    public static ReviewDTO fromReview(UriInfo uriInfo, Review r){
        final ReviewDTO dto = new ReviewDTO();
        dto.reviewerId = r.getReviewer().getUserId();
        dto.rating = r.getRating();
        dto.review = r.getReview();
        dto.date = r.getDate();

        dto.self = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(r.getBookId())).path("reviews").path(String.valueOf(r.getReviewer().getUserId())).build();
        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(r.getBookId())).build();
        dto.reviewer = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(r.getReviewer().getUserId())).build();

        return dto;
    }

    public long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getBook() {
        return book;
    }

    public void setBook(URI book) {
        this.book = book;
    }

    public URI getReviewer() {
        return reviewer;
    }

    public void setReviewer(URI reviewer) {
        this.reviewer = reviewer;
    }
}

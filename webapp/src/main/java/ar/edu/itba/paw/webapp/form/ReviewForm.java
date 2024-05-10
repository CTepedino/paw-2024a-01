package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.*;

public class ReviewForm {

    @Min(0)
    @Max(10)
    private int rating;

    @NotNull
    @Size(min = 1, max = 255)
    private String review;

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
}

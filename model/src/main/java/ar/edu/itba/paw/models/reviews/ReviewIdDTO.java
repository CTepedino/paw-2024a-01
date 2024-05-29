package ar.edu.itba.paw.models.reviews;

public class ReviewIdDTO {
    private long bookId;
    private long reviewerId;

    public ReviewIdDTO(long bookId, long reviewerId) {
        this.bookId = bookId;
        this.reviewerId = reviewerId;
    }

    public long getBookId() {
        return bookId;
    }

    public long getReviewerId() {
        return reviewerId;
    }
}

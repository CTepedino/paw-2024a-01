package ar.edu.itba.paw.models.reviews;

import ar.edu.itba.paw.models.users.User;

import java.io.Serializable;
import java.util.Objects;

public class ReviewKey implements Serializable {
    private Long bookId;
    private User reviewer;

    protected ReviewKey() {}

    public ReviewKey(final Long bookId, final User reviewer) {
        this.bookId = bookId;
        this.reviewer = reviewer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewKey reviewKey = (ReviewKey) o;
        return reviewer.getUserId()==reviewKey.reviewer.getUserId() && bookId.equals(reviewKey.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewer.getUserId(), bookId);
    }
}
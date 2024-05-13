package ar.edu.itba.paw.webapp.form;


import ar.edu.itba.paw.models.reviews.ReviewOrderBy;

import javax.validation.constraints.NotNull;

public class ReviewSortForm {

    @NotNull
    private ReviewOrderBy orderBy = ReviewOrderBy.DATE_DESC;

    public ReviewOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(ReviewOrderBy orderBy) {
        this.orderBy = orderBy;
    }
}

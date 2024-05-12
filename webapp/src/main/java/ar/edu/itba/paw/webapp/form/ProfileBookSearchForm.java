package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.books.BookSearchOrderBy;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProfileBookSearchForm {

    private String title;

    @NotNull
    private BookSearchOrderBy orderBy = BookSearchOrderBy.PUBLICATION_DATE_DESC;

    @NotNull
    @Min(1)
    private Integer page = 1;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public BookSearchOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(BookSearchOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}

package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;

import javax.validation.constraints.*;

public class BookSearchForm {

    @NotNull
    @NotEmpty
    private String title;

    private BookGenre genre;

    @PositiveOrZero
    private Double minPrice;

    @PositiveOrZero
    private Double maxPrice;

    @PositiveOrZero
    private Integer minPageCount;

    @PositiveOrZero
    private Integer maxPageCount;

    @PositiveOrZero
    private Integer minSuggestedAge;

    @PositiveOrZero
    private Integer maxSuggestedAge;

    @NotNull
    private BookSearchOrderBy orderBy = BookSearchOrderBy.PUBLICATION_DATE_ASC;

    @NotNull
    @Min(1)
    private Integer page = 1;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BookGenre getGenre() {
        return genre;
    }

    public void setGenre(BookGenre genre) {
        this.genre = genre;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinPageCount() {
        return minPageCount;
    }

    public void setMinPageCount(Integer minPageCount) {
        this.minPageCount = minPageCount;
    }

    public Integer getMaxPageCount() {
        return maxPageCount;
    }

    public void setMaxPageCount(Integer maxPageCount) {
        this.maxPageCount = maxPageCount;
    }

    public Integer getMinSuggestedAge() {
        return minSuggestedAge;
    }

    public void setMinSuggestedAge(Integer minSuggestedAge) {
        this.minSuggestedAge = minSuggestedAge;
    }

    public Integer getMaxSuggestedAge() {
        return maxSuggestedAge;
    }

    public void setMaxSuggestedAge(Integer maxSuggestedAge) {
        this.maxSuggestedAge = maxSuggestedAge;
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

package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.BookGenre;
import ar.edu.itba.paw.models.BookSearchOrderBy;

public class BookSearchForm {

    private String title;
    private BookGenre genre;
    private Double minPrice;
    private Double maxPrice;
    private Integer minPageCount;
    private Integer maxPageCount;
    private Integer minSuggestedAge;
    private Integer maxSuggestedAge;
    private BookSearchOrderBy orderBy;
    private boolean asc = true;

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

    public boolean getAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}

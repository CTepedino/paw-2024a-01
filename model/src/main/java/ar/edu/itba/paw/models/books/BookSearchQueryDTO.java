package ar.edu.itba.paw.models.books;

import java.math.BigDecimal;

public class BookSearchQueryDTO {

    private String title;
    private BookGenre genre;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private Integer minPageCount;
    private Integer maxPageCount;

    private Integer minSuggestedAge;
    private Integer maxSuggestedAge;

    private Long writerId;

    private Long ownerId;
    private boolean recommendedByUserOnly;

    private Long recommendationsForId;

    private BookSearchOrderBy orderBy;

    private int pageNumber;
    private int pageSize;


    public BookSearchQueryDTO() {}

    public BookSearchQueryDTO(String title, BookGenre genre, BigDecimal minPrice, BigDecimal maxPrice, Integer minPageCount, Integer maxPageCount, Integer minSuggestedAge, Integer maxSuggestedAge, Long writerId, Long ownerId, Long recommendationsForId, BookSearchOrderBy orderBy, int pageNumber, int pageSize) {
        this.title = title;
        this.genre = genre;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minPageCount = minPageCount;
        this.maxPageCount = maxPageCount;
        this.minSuggestedAge = minSuggestedAge;
        this.maxSuggestedAge = maxSuggestedAge;
        this.orderBy = orderBy;
        this.writerId = writerId;
        this.ownerId = ownerId;
        this.recommendationsForId = recommendationsForId;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.recommendedByUserOnly = false;
    }

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

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
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

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getRecommendationsForId() {
        return recommendationsForId;
    }

    public void setRecommendationsForId(Long recommendationsForId) {
        this.recommendationsForId = recommendationsForId;
    }

    public BookSearchOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(BookSearchOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset(){
        return (pageNumber -1) * pageSize;
    }

    public int getLimit(){
        return pageSize;
    }

    public void setRecommendedByUserOnly(boolean recommendedByUserOnly) {
        this.recommendedByUserOnly = recommendedByUserOnly;
    }

    public boolean isRecommendedByUserOnly() {
        return recommendedByUserOnly;
    }
}

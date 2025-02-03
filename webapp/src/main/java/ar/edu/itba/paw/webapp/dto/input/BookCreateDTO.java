package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.webapp.dto.input.validations.TodayOrBefore;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BookCreateDTO {

    @NotNull
    @Size(min = 1, max = 50)
    private String title;

    @NotNull
    @Size(min = 1, max = 2500)
    private String description;

    @NotNull
    private BookGenre genre;

    @NotNull
    @PositiveOrZero
    @Max(value = 100)
    private Integer suggestedAge;

    @NotNull
    @PositiveOrZero
    @DecimalMax(value = "10000000.0")
    private BigDecimal price;

    @NotNull
    @Positive
    @Max(value = 1000000)
    private Integer pageCount;

    @TodayOrBefore
    @NotNull
    private LocalDate publicationDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BookGenre getGenre() {
        return genre;
    }

    public void setGenre(BookGenre genre) {
        this.genre = genre;
    }

    public Integer getSuggestedAge() {
        return suggestedAge;
    }

    public void setSuggestedAge(Integer suggestedAge) {
        this.suggestedAge = suggestedAge;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
}

package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.webapp.dto.input.validations.ImageFile;
import ar.edu.itba.paw.webapp.dto.input.validations.TodayOrBefore;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BookCreateDTO {

    @NotNull
    @Size(min = 1, max = 50)
    private String title;

    @NotNull
    @Size(min = 1, max = 1000)
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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd/MM/yyyy")
    @TodayOrBefore
    @NotNull
    private LocalDate publicationDate;

    private long writerId;

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

    public long getWriterId() {
        return writerId;
    }

    public void setWriterId(long writerId) {
        this.writerId = writerId;
    }
}

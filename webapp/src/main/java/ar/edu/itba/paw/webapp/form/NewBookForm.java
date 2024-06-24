package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.webapp.form.validations.FileExists;
import ar.edu.itba.paw.webapp.form.validations.ImageFile;
import ar.edu.itba.paw.webapp.form.validations.PdfFile;
import ar.edu.itba.paw.webapp.form.validations.TodayOrBefore;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NewBookForm {

    @Size(min = 1, max=50)
    private String title;

    @Size(min = 1, max=1000)
    private String description;


    private BookGenre genre;

    @NotNull
    @PositiveOrZero
    @Max(value = 100)
    private Integer suggestedAge;

    @NotNull
    @PositiveOrZero
    @DecimalMax(value = "100000000.0")
    private BigDecimal price;

    @NotNull
    @Positive
    @Max(value = 1000000)
    private Integer pageCount;

    @NotNull
    @FileExists
    @ImageFile
    private MultipartFile cover;

    @NotNull
    @FileExists
    @PdfFile
    private MultipartFile preview;

    @NotNull
    @FileExists
    @PdfFile
    private MultipartFile bookFile;

    @Size(min = 6, max = 22)
    @Pattern(regexp = "[a-zA-ZáéíóúüÁÉÍÓÚÜñÑ0-9.-]+")
    private String cbu;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd/MM/yyyy")
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

    public MultipartFile getCover() {
        return cover;
    }

    public void setCover(MultipartFile cover) {
        this.cover = cover;
    }

    public MultipartFile getPreview() {
        return preview;
    }

    public void setPreview(MultipartFile preview) {
        this.preview = preview;
    }


    public MultipartFile getBookFile() {
        return bookFile;
    }

    public void setBookFile(MultipartFile bookFile) {
        this.bookFile = bookFile;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
}

package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.BookGenre;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

public class NewBookForm {

    @Size(min = 1, max=255)
    private String title;

    @Size(min = 1, max=255)
    private String description;


    private BookGenre genre;

    @NotNull
    @PositiveOrZero
    private Integer suggestedAge;

    @NotNull
    @PositiveOrZero
    private Double price;

    @NotNull
    @Positive
    private Integer pageCount;

    @NotNull
    private MultipartFile image;

    @NotNull
    private MultipartFile pdf;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public MultipartFile getPdf() {
        return pdf;
    }

    public void setPdf(MultipartFile pdf) {
        this.pdf = pdf;
    }
}

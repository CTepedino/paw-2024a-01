package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.BookGenre;
import org.springframework.web.multipart.MultipartFile;

public class NewBookForm {
    private String writerFirstName;
    private String writerLastName;
    private String writerEmail;

    private String title;
    private String description;
    private BookGenre genre;
    private int suggestedAge;
    private double price;
    private int pageCount;

    private MultipartFile image;
    private MultipartFile pdf;

    public String getWriterFirstName() {
        return writerFirstName;
    }

    public void setWriterFirstName(String writerFirstName) {
        this.writerFirstName = writerFirstName;
    }

    public String getWriterLastName() {
        return writerLastName;
    }

    public void setWriterLastName(String writerLastName) {
        this.writerLastName = writerLastName;
    }

    public String getWriterEmail() {
        return writerEmail;
    }

    public void setWriterEmail(String writerEmail) {
        this.writerEmail = writerEmail;
    }

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

    public int getSuggestedAge() {
        return suggestedAge;
    }

    public void setSuggestedAge(int suggestedAge) {
        this.suggestedAge = suggestedAge;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
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

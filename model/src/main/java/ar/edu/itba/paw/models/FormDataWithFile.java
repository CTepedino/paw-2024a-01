package ar.edu.itba.paw.models;

import org.springframework.web.multipart.MultipartFile;

public class FormDataWithFile {
    private String writer_name;
    private String writer_lastname;
    private String writer_email;
    private String title;
    private String description;

    private String genre;

    private int page_numbers;

    private String prev;

    private int suggested_age;

    private Double price;

    private MultipartFile file;


    private String published_date;





    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getWriter_name(){
        return writer_name;
    }


    public String getWriter_lastname(){
        return writer_lastname;
    }

    public String getWriter_email() {
        return writer_email;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getPage_numbers() {
        return page_numbers;
    }

    public void setPage_numbers(int page_numbers) {
        this.page_numbers = page_numbers;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public int getSuggested_age() {
        return suggested_age;
    }

    public void setSuggested_age(int suggested_age) {
        this.suggested_age = suggested_age;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public void setWriter_email(String writer_email) {
        this.writer_email = writer_email;
    }

    public void setWriter_lastname(String writer_lastname) {
        this.writer_lastname = writer_lastname;
    }

    public void setWriter_name(String writer_name){
        this.writer_name = writer_name;
    }



}

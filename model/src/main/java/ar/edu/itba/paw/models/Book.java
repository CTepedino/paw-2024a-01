package ar.edu.itba.paw.models;

import java.io.File;
import java.util.Date;

public class Book {
        private final long libroId;
        private final String title;
        private final String description;
        private final String genre;
        private final Double price;
        private final int pageNumbers;
        private final String prev;
        private final Long image_id;
        private final int suggestedAge;

        private final String published_date;

        private final Long writer_id;


        public Book(long libroId, String title, String description, String genre, Double price, int pageNumbers, String prev, Long image_id, int suggestedAge, String published_date, Long writer_id){
            this.libroId=libroId;
            this.title = title;
            this.description = description;
            this.genre=genre;
            this.price=price;
            this.pageNumbers=pageNumbers;
            this.prev=prev;
            this.image_id=image_id;
            this.suggestedAge=suggestedAge;
            this.published_date=published_date;
            this.writer_id=writer_id;
        }


    public long getLibroId() {
        return libroId;
    }
    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public long getImage_id() {
        return image_id;
    }

    public long getWriter_id() {
        return writer_id;
    }

    public int getPageNumbers() {
        return pageNumbers;
    }

    public String getpublished_date() {
        return published_date;
    }

    public String getDescription() {
        return description;
    }

    public String getGenra() {
        return genre;
    }

    public int getSuggestedAge() {
        return suggestedAge;
    }

    public Long findbyid() {
        return image_id;
    }

    public String getPrev() {
        return prev;
    }
}

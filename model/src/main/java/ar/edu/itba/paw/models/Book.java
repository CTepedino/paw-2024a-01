package ar.edu.itba.paw.models;

import java.util.Date;

public class Book {
        private final int libroId;
        private final String title;
        /*private final String description;
        private final String genra;
        private final double price;
        private final int pageNumbers;
        private final String prev;
        private final String image;
        private final int suggestedAge;

        private final String publishedDate;

        private final String writerEmail;*/

        public Book(int libroId, String title /*, String description, String genra, double price, int pageNumbers, String prev, String image, int suggestedAge, String publishedDate, String writerEmail*/){
            this.libroId=libroId;
            this.title = title;
           /* this.description = description;
            this.genra=genra;
            this.price=price;
            this.pageNumbers=pageNumbers;
            this.prev=prev;
            this.image=image;
            this.suggestedAge=suggestedAge;
            this.publishedDate=publishedDate;
            this.writerEmail=writerEmail;*/
        }


    public int getLibroId() {
        return libroId;
    }
    public String getTitle() {
        return title;
    }
/*
    public double getPrice() {
        return price;
    }

    public String getWriterEmail() {
        return writerEmail;
    }



    public int getPageNumbers() {
        return pageNumbers;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public String getGenra() {
        return genra;
    }

    public int getSuggestedAge() {
        return suggestedAge;
    }

    public String getImage() {
        return image;
    }

    public String getPrev() {
        return prev;
    }*/
}

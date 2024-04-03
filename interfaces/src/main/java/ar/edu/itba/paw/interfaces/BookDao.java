package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;

import java.util.Date;

public interface BookDao {
    Book create(String title, String description, String genra, Double price, int pageNumbers, String prev, String image, int suggestedAge, String publishedDate, String writerEmail);
}

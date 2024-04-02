package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;


public interface BookService {
    Book create(String title /*, String description, String genra, double price, int pageNumbers, String prev, String image, int suggestedAge, String publishedDate, String writerEmail*/);
}

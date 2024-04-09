package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;

import java.util.List;


public interface BookService {
    Book create(String title, String description, String genre, Double price, int pageNumbers, String prev, long image_id, int suggestedAge, String published_date, long writer_id);

    public List<Book> getBooks();
}

package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;

import java.util.List;
import java.util.Optional;


public interface BookService {
    Book create(String title, String description, String genre, Double price, int pageNumbers, String prev, long image_id, int suggestedAge, String published_date, String writerName, String writerSurname, String writerEmail);

    public Optional<Book> findById(long id);
    public List<Book> getBooks();
}

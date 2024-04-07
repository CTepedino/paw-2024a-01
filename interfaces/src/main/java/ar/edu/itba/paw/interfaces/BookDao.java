package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Writer;

import java.util.Date;
import java.util.Optional;

public interface BookDao {
    Optional<Book> findById(long id);
    Book create(String title, String description, String genre, Double price, int pageNumbers, String prev, long image_id, int suggestedAge, String published_date, long writer_id);
}

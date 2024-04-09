package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.BookDao;
import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Autowired
    public BookServiceImpl(final BookDao bookDao){
        this.bookDao = bookDao;
    }

    @Override
    public List<Book> getBooks(){
        return bookDao.getBooks();
    }

    @Override
    public Book create(String title, String description, String genre, Double price, int page_numbers, String prev, long image_id, int suggestedAge, String published_date, String writerName, String writerSurname, String writerEmail) {
        return bookDao.create(title,description,genre,price,page_numbers,prev,image_id,suggestedAge,published_date,writerName, writerSurname, writerEmail);
    }


}

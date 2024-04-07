package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.BookDao;
import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Autowired
    public BookServiceImpl(final BookDao bookDao){
        this.bookDao = bookDao;
    }

    @Override
    public Book create(String title, String description, String genre, Double price, int page_numbers, String prev, long image_id, int suggestedAge, String published_date, long writer_id) {
        return bookDao.create(title,description,genre,price,page_numbers,prev,image_id,suggestedAge,published_date,writer_id);
    }


}

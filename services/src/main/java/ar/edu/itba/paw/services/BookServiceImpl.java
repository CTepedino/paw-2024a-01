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
    /*
    @Override
    public Book create(String title, String description, String genra, double price, int page_numbers, String prev, String image, int suggestedAge, String publishedDate, String writerEmail) {
        return bookDao.create(title,description,genra,price,page_numbers,prev,image,suggestedAge,publishedDate,writerEmail);
    }
*/
    @Override
    public Book create(String title) {
        return bookDao.create(title);
    }

}

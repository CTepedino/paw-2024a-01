package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookGenreOrderBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final BookDao bookDao;

    private final static Logger LOGGER = LoggerFactory.getLogger(GenreServiceImpl.class);

    @Autowired
    public GenreServiceImpl(final BookDao bookDao){
        this.bookDao = bookDao;
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<BookGenre> getGenres(BookGenreOrderBy orderBy, int pageNumber, int pageSize) {
        List<BookGenre> genres;

        if (orderBy == BookGenreOrderBy.BOOK_COUNT){
            genres = bookDao.getGenresByBookCount((pageNumber - 1) * pageSize, pageSize);
            List<BookGenre> booklessGenres = List.of(BookGenre.values());
            int i = 0;
            while (genres.size() < pageSize) {
                BookGenre genre = booklessGenres.get(i);
                if (!genres.contains(genre)) {
                    genres.add(genre);
                }
                i++;
            }
        } else {
            genres = List.of(BookGenre.values()).subList((pageNumber-1)*pageSize, pageNumber * pageSize);
        }

        LOGGER.atDebug().setMessage("Retrieved book genre list").log();

        return new PaginatedContent<>(genres, pageNumber, pageSize, BookGenre.values().length);
    }

}

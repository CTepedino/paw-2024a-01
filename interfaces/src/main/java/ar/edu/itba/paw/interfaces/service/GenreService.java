package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookGenreOrderBy;

public interface GenreService {

    PaginatedContent<BookGenre> getGenres(BookGenreOrderBy orderBy, int pageNumber, int pageSize);
}

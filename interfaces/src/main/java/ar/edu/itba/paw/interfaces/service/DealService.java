package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookAndDeal;
import ar.edu.itba.paw.models.deals.Deal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DealService {

    void create(long bookId, BigDecimal price, int duration);

    Optional<Deal> get(long bookId);

    List<BookAndDeal> get(List<Book> books);

    public void update(long bookId, BigDecimal price, int duration);

    void endDeal(long bookId);
}

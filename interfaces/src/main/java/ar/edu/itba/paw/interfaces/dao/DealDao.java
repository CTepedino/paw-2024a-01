package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.deals.Deal;
import ar.edu.itba.paw.models.users.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DealDao {

    Deal create(long bookId, BigDecimal price, LocalDate startDate,LocalDate endDate);

    void update(Deal bookId, BigDecimal price, LocalDate endDate);

    Optional<Deal> findById(long dealId);

    void deleteDeal(long dealId);

    List<Deal> getAll();

    List<Book> getNewDeals(int size);

}

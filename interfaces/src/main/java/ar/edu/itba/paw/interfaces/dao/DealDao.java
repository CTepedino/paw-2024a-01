package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.deals.Deal;
import ar.edu.itba.paw.models.users.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface DealDao {

    Deal create(long bookId, BigDecimal price, LocalDate startDate,LocalDate endDate);

    Optional<Deal> find(long bookId);

    void update(Deal bookId, BigDecimal price, LocalDate endDate);

}

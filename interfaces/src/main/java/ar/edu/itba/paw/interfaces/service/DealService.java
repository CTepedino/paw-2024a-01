package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.deals.Deal;

import java.math.BigDecimal;
import java.util.Optional;

public interface DealService {

    void createOrUpdate(long bookId, BigDecimal price, int duration);

    Optional<Deal> get(long bookId);

    void endDeal(long bookId);

    void deleteExpiredDeals();

}

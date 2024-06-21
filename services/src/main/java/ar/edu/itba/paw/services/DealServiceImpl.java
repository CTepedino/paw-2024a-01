package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.DealDao;
import ar.edu.itba.paw.interfaces.service.DealService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.deals.Deal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DealServiceImpl implements DealService {

    private final DealDao dealDao;

    @Autowired
    public DealServiceImpl(DealDao dealDao){
        this.dealDao=dealDao;
    }

    @Transactional
    @Override
    public void create(long bookId, BigDecimal price, int duration) {
        if(get(bookId).isEmpty()) {
            dealDao.create(bookId, price, LocalDate.now(), LocalDate.now().plusDays(duration));
        }
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<Deal> get(long bookId) {
        dealDao.findById(bookId);
        return dealDao.findById(bookId);

    }

    @Transactional
    @Override
    public void update(long dealId, BigDecimal price, int duration) {
        Optional<Deal> deal = dealDao.findById(dealId);
        deal.ifPresent(value -> dealDao.update(value, price, value.getStartDate().plusDays(duration)));
    }

    @Transactional
    @Override
    public void endDeal(long dealId) {
        dealDao.deleteDeal(dealId);
    }

}

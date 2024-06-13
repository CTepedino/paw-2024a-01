package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.DealDao;
import ar.edu.itba.paw.interfaces.service.DealService;
import ar.edu.itba.paw.models.deals.Deal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        dealDao.create(bookId , price, LocalDate.now(), LocalDate.now().plusDays(duration));
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<Deal> get(long bookId) {
        return dealDao.find(bookId);
    }

    @Transactional
    @Override
    public void update(long bookId, BigDecimal price, int duration) {
        Optional<Deal> deal = get(bookId);
        deal.ifPresent(value -> dealDao.update(value, price, value.getStartDate().plusDays(duration)));
    }

    @Transactional
    @Override
    public void endDeal(long bookId, BigDecimal price) {
        Optional<Deal> deal = get(bookId);
        deal.ifPresent(value -> dealDao.update(value, price, LocalDate.now().minusDays(1)));
    }


}

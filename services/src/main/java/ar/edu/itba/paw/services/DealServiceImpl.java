package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.DealDao;
import ar.edu.itba.paw.interfaces.service.DealService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.deals.Deal;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.InvalidDealException;
import ar.edu.itba.paw.models.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final BookDao bookDao;

    private final static Logger LOGGER = LoggerFactory.getLogger(DealServiceImpl.class);
    @Autowired
    public DealServiceImpl(final DealDao dealDao, final BookDao bookDao){
        this.dealDao = dealDao;
        this.bookDao = bookDao;
    }

    @Transactional
    @Override
    public void createOrUpdate(long bookId, BigDecimal price, int duration) {
        Book book = bookDao.findById(bookId).orElseThrow(BookNotFoundException::new);

        if (price.compareTo(book.getPrice().multiply(new BigDecimal("0.95").setScale(2, RoundingMode.HALF_UP))) >= 0){
            throw new InvalidDealException();
        }

        if (book.getDeal() == null){
            dealDao.create(bookId, price, LocalDate.now(), LocalDate.now().plusDays(duration));
            LOGGER.atDebug().setMessage("Started deal for book {}").addArgument(bookId).log();
        } else {
            dealDao.update(book.getDeal(), price, book.getDeal().getStartDate().plusDays(duration));
            LOGGER.atDebug().setMessage("Updated deal for book {}").addArgument(bookId).log();
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
    public void endDeal(long dealId) {
        dealDao.deleteDeal(dealId);
        LOGGER.atDebug().setMessage("Ended deal for book {}").addArgument(dealId).log();
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void deleteExpiredDeals(){
        for (Deal deal: dealDao.getAll()){
            if(deal.getEndDate().isBefore(LocalDate.now())){
                dealDao.deleteDeal(deal.getDealId());
            }

        }
    }

}

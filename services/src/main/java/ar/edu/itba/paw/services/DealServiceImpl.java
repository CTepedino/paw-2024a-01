package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.DealDao;
import ar.edu.itba.paw.interfaces.service.DealService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookAndDeal;
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
        List<Deal> list = dealDao.find(bookId);
        return list.stream()
                .filter(deal -> deal.getEndDate().isAfter(LocalDate.now()) || deal.getEndDate().isEqual(LocalDate.now()))
                .findFirst();

    }

    @Transactional(readOnly = true)
    @Override
    public List<BookAndDeal> get(List<Book> books) {
        return books.stream()
                .map(book -> {
                    Optional<Deal> dealOptional = get(book.getBookId());
                    Deal deal = dealOptional.orElse(null);
                    return new BookAndDeal(book, deal);
                })
                .toList();
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
        Optional<Deal> deal = dealDao.findById(dealId);
        deal.ifPresent(value -> dealDao.update(value, deal.get().getPrice(), LocalDate.now().minusDays(1)));
    }

    @Override
    public String getPercentage(Book book, Deal deal){
        if(book == null || deal == null ){
            return null;
        }

        BigDecimal change = deal.getPrice().subtract(book.getPrice());
        BigDecimal percentageChange = change.divide(book.getPrice(), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        return String.format("%+d%%", percentageChange.intValue());
    }


}

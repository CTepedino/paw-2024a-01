package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DealDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.deals.Deal;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DealJpaDao implements DealDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Deal create(long bookId, BigDecimal price, LocalDate startDate, LocalDate endDate) {
        Deal deal = new Deal(bookId, price, startDate, endDate);
        em.persist(deal);
        return deal;
    }


    @Override
    public Optional<Deal> findById(long dealId) {
        TypedQuery<Deal> query = em.createQuery("FROM Deal d WHERE d.dealId = :dealId", Deal.class);
        query.setParameter("dealId", dealId);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Deal deal, BigDecimal price, LocalDate endDate) {
        deal.setPrice(price);
        deal.setEndDate(endDate);
    }

    @Override
    public void deleteDeal(long dealId){
        Query deleteQuery = em.createQuery("DELETE FROM Deal d WHERE d.dealId = :dealId");
        deleteQuery.setParameter("dealId", dealId);
        deleteQuery.executeUpdate();
    }

    @Override
    public List<Deal> getAll(){
        TypedQuery<Deal> query = em.createQuery("FROM Deal", Deal.class);
        return query.getResultList();
    }


}

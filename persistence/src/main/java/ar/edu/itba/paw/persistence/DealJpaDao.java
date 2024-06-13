package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DealDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.deals.Deal;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.users.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
    public Optional<Deal> find(long bookId) {
        TypedQuery<Deal> query = em.createQuery("FROM Deal d WHERE d.bookId = :bookId", Deal.class);
        query.setParameter("bookId", bookId);

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
}

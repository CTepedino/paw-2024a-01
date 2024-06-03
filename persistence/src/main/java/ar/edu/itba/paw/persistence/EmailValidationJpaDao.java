package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.EmailValidationDao;
import ar.edu.itba.paw.models.users.EmailValidation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class EmailValidationJpaDao implements EmailValidationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public EmailValidation create(long id, String code, LocalDateTime expiration) {
        EmailValidation emailValidation = new EmailValidation(id, code, expiration);
        em.persist(emailValidation);
        return emailValidation;
    }

    @Override
    public Optional<EmailValidation> get(long id) {
        return Optional.ofNullable(em.find(EmailValidation.class, id));
    }

    @Override
    public void deleteExpired() {
        Query query = em.createQuery("DELETE FROM EmailValidation ev WHERE ev.expiration < now()");
    }

    @Override
    public void delete(long id) {
        Query query = em.createQuery("DELETE FROM EmailValidation ev WHERE ev.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}

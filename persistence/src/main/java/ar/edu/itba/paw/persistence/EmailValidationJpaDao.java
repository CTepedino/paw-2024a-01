package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.EmailValidationDao;
import ar.edu.itba.paw.models.users.EmailValidation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class EmailValidationJpaDao implements EmailValidationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(long id, String code, LocalDateTime expiration) {

    }

    @Override
    public Optional<EmailValidation> get(long id) {
        return Optional.empty();
    }

    @Override
    public void deleteExpired() {

    }

    @Override
    public void delete(long id) {

    }
}

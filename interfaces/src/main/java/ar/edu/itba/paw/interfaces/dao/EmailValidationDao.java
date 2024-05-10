package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.users.EmailValidation;
import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailValidationDao {

    void create(long id, String code, LocalDateTime expiration);

    Optional<EmailValidation> get(long id);

    void deleteExpired();

    void delete(long id);

}

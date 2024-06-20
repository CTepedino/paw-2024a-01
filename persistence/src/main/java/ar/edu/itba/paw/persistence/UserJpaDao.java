package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.EmailValidation;
import ar.edu.itba.paw.models.users.ResetCode;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class UserJpaDao implements UserDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public User create(String email, String password, String firstName, String lastName, boolean isEnabled, Locale locale) {
        final User user = new User(email, password, firstName, lastName, isEnabled, locale);
        em.persist(user);
        return user;
    }

    @Override
    public void update(User user, String firstName, String lastName, String cbu, String description) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCbu(cbu);
        user.setDescription(description);
    }

    @Override
    public void updateIsEnabled(User user, boolean enabled) {
        user.setEnabled(enabled);
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(password);
    }

    @Override
    public ProfilePicture createProfilePicture(User user, byte[] profilePicture){
        ProfilePicture pfp = new ProfilePicture(user.getUserId(), profilePicture);
        em.persist(pfp);
        return pfp;
    }

    @Override
    public void updateProfilePicture(User user, byte[] profilePicture){
        user.getProfilePicture().setFile(profilePicture);
    }

    @Override
    public void giveRole(User user, UserRoles role) {
        user.getRoles().add(role);
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = em.createQuery("FROM User WHERE email = :email", User.class);
        query.setParameter("email", email);

        return query.getResultList().stream().findFirst();
    }

    @Override
    public void recheckAllPaused(long userId) {
        Query query = em.createQuery("""
            UPDATE Book b SET isPaused = CASE
                WHEN NOT EXISTS(
                    SELECT 1
                    FROM BookFile bf
                    WHERE bf.id = b.bookId
                ) OR EXISTS(
                    SELECT 1
                    FROM User AS u
                    WHERE u.userId = :userId AND u.cbu IS NULL
                ) THEN TRUE
                ELSE FALSE
                END
            WHERE b.writer.userId = :userId
        """);
        query.setParameter("userId",userId);
        query.executeUpdate();
    }

    @Override
    public List<User> getUsersWithPausedBooks() {
        TypedQuery<User> query = em.createQuery("SELECT DISTINCT u FROM User u WHERE EXISTS(SELECT 1 FROM Book b WHERE b.writer.userId = u.userId)", User.class);
        return query.getResultList();
    }

    @Override
    public EmailValidation createEmailValidation(long id, String code, LocalDateTime expiration) {
        EmailValidation ev = new EmailValidation(id, code, expiration);
        em.persist(ev);
        return ev;
    }

    @Override
    public void deleteExpiredEmailValidations() {
        Query query = em.createQuery("DELETE FROM EmailValidation ev WHERE ev.expiration < now()");
    }

    @Override
    public void deleteEmailValidation(long id) {
        Query query = em.createQuery("DELETE FROM EmailValidation ev WHERE ev.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public ResetCode createResetCode(long id, String code, LocalDateTime expiration) {
        ResetCode rc = new ResetCode(id, code, expiration);
        em.persist(rc);
        return rc;
    }

    @Override
    public void deleteExpiredResetCodes() {
        Query query = em.createQuery("DELETE FROM ResetCode rc WHERE rc.expiration < now()");
    }

    @Override
    public void deleteResetCode(long id) {
        Query query = em.createQuery("DELETE FROM ResetCode rc WHERE rc.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
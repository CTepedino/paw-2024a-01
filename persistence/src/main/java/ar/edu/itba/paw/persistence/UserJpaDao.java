package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
    public int update(long id, String email, String password, String firstName, String lastName, String cbu, boolean isEnabled, Locale locale, String description) {
        Optional<User> maybeUser = findById(id);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setCbu(cbu);
            user.setEnabled(isEnabled);
            user.setDescription(description);
            em.merge(user);
            return 1;
        }
        return 0;
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
    public int giveRole(long id, UserRoles role) {
        return 0;
    }

    @Override
    public List<UserRoles> getRoles(long id) {
        return List.of();
    }


    //TODO: despues de books
    @Override
    public void recheckAllPaused(long userId) {
    }

    //TODO: despues de books
    @Override
    public List<User> getUsersWithPausedBooks() {
        return List.of();
    }
}
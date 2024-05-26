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
        return null;
    }

    @Override
    public int update(long id, String email, String password, String firstName, String lastName, boolean isEnabled) {
        return 0;
    }

    @Override
    public int update(long id, String email, String password, String firstName, String lastName, String cbu, boolean isEnabled, String description) {
        return 0;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public int giveRole(long id, UserRoles role) {
        return 0;
    }

    @Override
    public List<UserRoles> getRoles(long id) {
        return List.of();
    }

    @Override
    public void recheckAllPaused(long userId) {

    }

    @Override
    public List<User> getUsersWithPausedBooks() {
        return List.of();
    }
}
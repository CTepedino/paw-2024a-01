package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface UserDao {
    User create(String email, String password, String firstName, String lastName, boolean isEnabled, Locale locale);

    int update(long id, String email, String password, String firstName, String lastName, boolean isEnabled);

    int update(long id, String email, String password, String firstName, String lastName, String cbu, boolean isEnabled);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    int giveRole(long id, UserRoles role);


    List<UserRoles> getRoles(long id);

    void recheckAllPaused(long userId);

    List<User> getUsersWithPausedBooks();
}

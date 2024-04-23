package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserRoles;

import java.util.Optional;

public interface UserDao {
    User create(UserRoles[] roles, String email, String firstName, String lastName, String password);

    Optional<User> findById(long id);

    User giveRole(long id, UserRoles role);

    Optional<User> findByEmail(String email);

    void setNames(long id, String firstName, String lastName);

    void changePassword(long id, String password);
}

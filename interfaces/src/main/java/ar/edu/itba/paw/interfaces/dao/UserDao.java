package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(String email, String password, String firstName, String lastName);

    void delete(long id);

    int update(long id, String email, String password, String firstName, String lastName);

    int update(long id, String email, String password, String firstName, String lastName, String cbu);

    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);

    int giveRole(long id, UserRoles role);

    void removeRole(long id, UserRoles role);

    List<UserRoles> getRoles(long id);


}

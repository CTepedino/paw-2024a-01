package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User create(String email, String password, String firstName, String lastName);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    List<UserRoles> getRoles(long id);

    void giveWriterRole(long id);

/*    void fillMissingWriterData(long id, String password);*/

    void changePassword(long id, String password);

    Optional<User> getLoggedUser();
}

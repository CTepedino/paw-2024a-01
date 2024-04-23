package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(long id);

    User create(String email, String password);

    Optional<User> findByEmail(String email);

    void giveWriterRole(long id, String firstName, String lastName);

    Optional<User> getLoggedUser();

    void fillMissingWriterData(long id, String password);

    public void changePassword(long id, String password);
}

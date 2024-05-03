package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(long id);

    User create(String firstName, String lastName, String email, String password);

    Optional<User> findByEmail(String email);

    void giveWriterRole(long id);

    Optional<User> getLoggedUser();

    void fillMissingWriterData(long id, String password);

    public void changePassword(long id, String password);
}

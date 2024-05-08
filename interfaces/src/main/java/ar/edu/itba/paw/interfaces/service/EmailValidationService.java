package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.users.User;

public interface EmailValidationService {

    void create(User user);
    boolean checkValidation(long id, String email, String code);

    void deleteExpired();

    void resend(User user);
}

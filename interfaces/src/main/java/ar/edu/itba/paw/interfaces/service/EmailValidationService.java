package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.users.EmailValidation;
import ar.edu.itba.paw.models.users.User;

public interface EmailValidationService {

    EmailValidation create(User user);

    boolean checkValidation(long id, String code);

    void deleteExpired();

    void resend(User user);

    boolean isEmailValidationCode(String code);
}

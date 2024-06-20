package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.users.ResetCode;
import ar.edu.itba.paw.models.users.User;

public interface ResetCodeService {

    ResetCode create(User user);

    boolean checkResetCode(long id, String code);

    void deleteExpired();

    void resend(User user);
}

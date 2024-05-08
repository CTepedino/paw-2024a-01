package ar.edu.itba.paw.interfaces.service;

public interface EmailValidationService {

    void create(long id);
    boolean checkValidation(long id, String email, String code);

}

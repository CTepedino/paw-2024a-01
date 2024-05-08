package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.EmailValidationDao;
import ar.edu.itba.paw.interfaces.service.EmailValidationService;
import ar.edu.itba.paw.models.exception.NoValidationCodeException;
import ar.edu.itba.paw.models.users.EmailValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailValidationServiceImpl implements EmailValidationService {


    private static final int VALIDATION_CODE_HOURS = 12;
    private static final int VALIDATION_CODE_LENGTH = 5;

    private final EmailValidationDao emailValidationDao;

    @Autowired
    public EmailValidationServiceImpl(EmailValidationDao emailValidationDao){
        this.emailValidationDao = emailValidationDao;
    }

    @Override
    public void create(long id) {
        emailValidationDao.create(id, generateRandomVerificationCode(), LocalDateTime.now().plusHours(VALIDATION_CODE_HOURS));
    }

    private String generateRandomVerificationCode(){
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < VALIDATION_CODE_LENGTH; i++){
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    @Override
    public boolean checkValidation(long id, String email, String code) {
        emailValidationDao.deleteExpired();

        EmailValidation validation = emailValidationDao.get(id).orElseThrow(NoValidationCodeException::new);
        if( validation.getCode().equals(code) && validation.getEmail().equals(email)){
            emailValidationDao.delete(id);
            return true;
        }
        return false;
    }
}

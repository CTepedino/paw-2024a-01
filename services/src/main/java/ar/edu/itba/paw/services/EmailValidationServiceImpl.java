package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.EmailValidationDao;
import ar.edu.itba.paw.interfaces.service.EmailValidationService;
import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.models.exception.NoValidationCodeException;
import ar.edu.itba.paw.models.users.EmailValidation;
import ar.edu.itba.paw.models.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailValidationServiceImpl implements EmailValidationService {


    private static final int VALIDATION_CODE_HOURS = 12;
    private static final int VALIDATION_CODE_LENGTH = 5;

    private final EmailValidationDao emailValidationDao;

    private final MailService ms;

    @Autowired
    public EmailValidationServiceImpl(EmailValidationDao emailValidationDao, MailService ms){
        this.emailValidationDao = emailValidationDao;
        this.ms = ms;
    }

    @Transactional
    @Override
    public void create(User user) {
        String code = generateRandomVerificationCode();
        LocalDateTime expiration = LocalDateTime.now().plusHours(VALIDATION_CODE_HOURS);
        emailValidationDao.create(user.getUserId(), code, expiration);
        ms.sendRegisterEmail(user, code, expiration);
    }

    private String generateRandomVerificationCode(){
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < VALIDATION_CODE_LENGTH; i++){
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    @Transactional
    @Override
    public void deleteExpired(){
        emailValidationDao.deleteExpired();
    }


    @Transactional
    @Override
    public boolean checkValidation(long id, String email, String code) {
        deleteExpired();

        EmailValidation validation = emailValidationDao.get(id).orElseThrow(NoValidationCodeException::new);
        if( validation.getCode().equals(code) && validation.getEmail().equals(email)){
            emailValidationDao.delete(id);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void resend(User user){
        deleteExpired();

        EmailValidation validation = emailValidationDao.get(user.getUserId()).orElseThrow(NoValidationCodeException::new);

        ms.sendRegisterEmail(user, validation.getCode(), validation.getExpiration());
    }
}

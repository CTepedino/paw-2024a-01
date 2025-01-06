package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.interfaces.service.ResetCodeService;
import ar.edu.itba.paw.models.exception.NoResetCodeException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.users.ResetCode;
import ar.edu.itba.paw.models.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ResetCodeServiceImpl implements ResetCodeService {

    private static final int RESET_CODE_HOURS = 12;
    private static final int RESET_CODE_LENGTH = 5;

    private final UserDao userDao;
    private final MailService ms;

    private final static Logger LOGGER = LoggerFactory.getLogger(ResetCodeServiceImpl.class);

    @Autowired
    public ResetCodeServiceImpl(final UserDao userDao, final MailService ms) {
        this.userDao = userDao;
        this.ms = ms;
    }

    @Transactional
    @Override
    public ResetCode create(User user) {
        deleteExpired();

        if (user.getResetCode() != null) {
            resend(user);
            return user.getResetCode();
        }

        String code = generateRandomResetCode();
        LocalDateTime expiration = LocalDateTime.now().plusHours(RESET_CODE_HOURS);
        ResetCode resetCode = userDao.createResetCode(user.getUserId(), code, expiration);

        ms.sendResetPasswordEmail(user, resetCode.getCode(), resetCode.getExpiration());
        LOGGER.atDebug().setMessage("Generated password reset code for user id {}").addArgument(user.getUserId()).log();

        return resetCode;
    }

    private String generateRandomResetCode(){
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < RESET_CODE_LENGTH; i++){
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    @Transactional
    @Override
    public boolean checkResetCode(long id, String code) {
        deleteExpired();

        ResetCode resetCode = userDao.findById(id).orElseThrow(UserNotFoundException::new).getResetCode();
        if(resetCode==null){
            throw new NoResetCodeException();
        }

        if(resetCode.getCode().equals(code)){
            userDao.deleteResetCode(id);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void deleteExpired() {
        userDao.deleteExpiredResetCodes();
    }

    @Transactional(readOnly = true)
    @Override
    public void resend(User user) {

        ResetCode resetCode = user.getResetCode();

        if(resetCode==null){
            throw new NoResetCodeException();
        }

        ms.sendResetPasswordEmail(user, resetCode.getCode(), resetCode.getExpiration());
    }
}

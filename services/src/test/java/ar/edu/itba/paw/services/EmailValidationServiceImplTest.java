package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.models.exception.NoValidationCodeException;
import ar.edu.itba.paw.models.users.EmailValidation;
import ar.edu.itba.paw.models.users.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailValidationServiceImplTest {

    private static final User TEST_USER = new User(1,"", "", "", "", false, Locale.ENGLISH);

    @Mock
    private MailService ms;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private EmailValidationServiceImpl emailValidationService;

    @Before
    public void setup(){
        Mockito.when(userDao.findById(Mockito.anyLong())).thenReturn(Optional.of(TEST_USER));
        Mockito.when(userDao.createEmailValidation(Mockito.anyLong(), Mockito.anyString(), Mockito.any())).thenReturn(new EmailValidation(1, "12345", LocalDateTime.now().plusHours(12)));
    }

    @Test
    public void testCreate(){
        LocalDateTime mimimumExpireTime = LocalDateTime.now().plusHours(11).plusMinutes(59);
        EmailValidation ev = emailValidationService.create(TEST_USER);

        assertEquals(1, ev.getId());
        assertEquals(5, ev.getCode().length());
        assertTrue(ev.getExpiration().isAfter(mimimumExpireTime));
    }

    @Test
    public void testCheckValidationInexistent() {
        TEST_USER.setEmailValidation(null);

        assertThrows(
                NoValidationCodeException.class,
                () -> emailValidationService.checkValidation(1, "12345")
        );
    }

    @Test
    public void testCheckValidationInvalidCode(){
        TEST_USER.setEmailValidation(new EmailValidation(1, "99999", null));

        boolean valid = emailValidationService.checkValidation(1, "12345");

        assertFalse(valid);

    }

    @Test
    public void testCheckValidationValidCode(){
        TEST_USER.setEmailValidation(new EmailValidation(1, "12345", null));

        boolean valid = emailValidationService.checkValidation(1, "12345");

        assertTrue(valid);
    }
}
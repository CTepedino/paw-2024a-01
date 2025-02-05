package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.models.exception.NoResetCodeException;
import ar.edu.itba.paw.models.users.ResetCode;
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
public class ResetCodeServiceImplTest {

    private static final User TEST_USER = new User(1,"", "", "", "", false, Locale.ENGLISH);

    @Mock
    private MailService ms;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private ResetCodeServiceImpl resetCodeService;

    @Before
    public void setup(){
        Mockito.when(userDao.findById(Mockito.anyLong())).thenReturn(Optional.of(TEST_USER));
        Mockito.when(userDao.createResetCode(Mockito.anyLong(), Mockito.anyString(), Mockito.any())).thenReturn(new ResetCode(1, "12345", LocalDateTime.now().plusHours(12)));
    }

    @Test
    public void testCreate(){
        LocalDateTime mimimumExpireTime = LocalDateTime.now().plusHours(11).plusMinutes(59);
        ResetCode rc = resetCodeService.create(TEST_USER);

        assertEquals(1, rc.getId());
        assertEquals(5, rc.getCode().length());
        assertTrue(rc.getExpiration().isAfter(mimimumExpireTime));
    }

    @Test
    public void testCheckValidationInexistent() {
        TEST_USER.setResetCode(null);

        assertThrows(
                NoResetCodeException.class,
                () -> resetCodeService.checkResetCode(1, "12345")
        );
    }

    @Test
    public void testCheckValidationInvalidCode(){
        TEST_USER.setResetCode(new ResetCode(1, "99999", null));

        boolean valid = resetCodeService.checkResetCode(1, "12345");

        assertFalse(valid);

    }

    @Test
    public void testCheckValidationValidCode(){
        TEST_USER.setResetCode(new ResetCode(1, "12345", null));

        boolean valid = resetCodeService.checkResetCode(1, "12345");

        assertTrue(valid);
    }
}

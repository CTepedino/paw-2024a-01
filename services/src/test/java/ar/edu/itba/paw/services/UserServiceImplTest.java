package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.EmailValidationService;
import ar.edu.itba.paw.models.exception.InvalidCodeException;
import ar.edu.itba.paw.models.exception.NoValidationCodeException;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String EMAIL = "johnDoe@mail.com";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "john";
    private static final String LAST_NAME = "doe";
    private static final String ENCODED_PASSWORD = PASSWORD + " encoded";

    private static final User TEST_USER = new User(1,"", "", "", "", true, Locale.US);


    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailValidationService evs;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup(){
        Mockito.when(userDao.findById(Mockito.anyLong())).thenReturn(Optional.of(TEST_USER));
    }

    @Test
    public void testCreate(){
        Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD)))
                .thenReturn(ENCODED_PASSWORD);
        Mockito.when(userDao.create(Mockito.eq(EMAIL), Mockito.eq(ENCODED_PASSWORD), Mockito.eq(FIRST_NAME), Mockito.eq(LAST_NAME), Mockito.eq(false), Mockito.any(Locale.class)))
                .thenReturn(new User(1, EMAIL,ENCODED_PASSWORD, FIRST_NAME, LAST_NAME, false, Locale.US));

        User user = userService.create(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);

        assertNotNull(user);
        assertEquals(EMAIL, user.getEmail());
        assertEquals(ENCODED_PASSWORD, user.getPassword());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertFalse(user.isEnabled());
        assertEquals(LocaleContextHolder.getLocale(), user.getLocale());
    }


    @Test
    public void testValidateInexistentUser(){
        Mockito.when(userDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(
                NoValidationCodeException.class,
                () -> userService.validateEmail(1, "12345")
        );
    }

    @Test
    public void testValidateEnabledUser(){
        Mockito.when(userDao.findById(Mockito.anyLong())).thenReturn(Optional.of(new User(1, EMAIL, ENCODED_PASSWORD, FIRST_NAME, LAST_NAME, true, Locale.US)));

        assertThrows(
                NoValidationCodeException.class,
                () -> userService.validateEmail(1, "12345")
        );
    }

    @Test
    public void testValidateInvalidCheck(){
        Mockito.when(userDao.findById(Mockito.anyLong())).thenReturn(Optional.of(new User(1, EMAIL, ENCODED_PASSWORD, FIRST_NAME, LAST_NAME, false, Locale.US)));
        Mockito.when(evs.checkValidation(Mockito.anyLong(), Mockito.anyString())).thenReturn(false);

        assertThrows(
                InvalidCodeException.class,
                () -> userService.validateEmail(1, "12345")
        );
    }
/*
    @Test
    public void testGetProfilePictureOrDefaultCaseDefault(){
        TEST_USER.setProfilePicture(null);

        ProfilePicture pfp = userService.getProfilePictureOrDefault(1);

        assertNotNull(pfp);
        assertNotNull(pfp.getFile());
    }

    @Test
    public void testGetProfilePictureOrDefaultCaseNonDefault(){
        byte[] img = new byte[]{0};
        TEST_USER.setProfilePicture(new ProfilePicture(1, img));

        ProfilePicture pfp = userService.getProfilePictureOrDefault(1);

        assertNotNull(pfp);
        assertEquals(img, pfp.getFile());
    }*/
}

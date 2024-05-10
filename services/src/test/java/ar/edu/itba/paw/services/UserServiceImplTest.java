package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.EmailValidationService;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String EMAIL = "johnDoe@mail.com";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "john";
    private static final String LAST_NAME = "doe";
    private static final String ENCODED_PASSWORD = PASSWORD + " encoded";


    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailValidationService evs;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testCreateOK(){
        Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD)))
                .thenReturn(ENCODED_PASSWORD);
        Mockito.when(userDao.create(Mockito.eq(EMAIL), Mockito.eq(ENCODED_PASSWORD), Mockito.eq(FIRST_NAME), Mockito.eq(LAST_NAME), Mockito.eq(false), Mockito.any(Locale.class)))
                .thenReturn(new User(1, EMAIL,ENCODED_PASSWORD, FIRST_NAME, LAST_NAME, false, Locale.US));
        List<UserRoles> mockRoles = new ArrayList<>();
        Mockito.when(userDao.giveRole(Mockito.anyLong(), Mockito.eq(UserRoles.READER))).thenAnswer((Answer<Void>) invocation -> {
            mockRoles.add(UserRoles.READER);
            return null;
        });
        ;

        User user = userService.create(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);


        assertNotNull(user);
        assertEquals(EMAIL, user.getEmail());
        assertEquals(ENCODED_PASSWORD, user.getPassword());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
    }
}

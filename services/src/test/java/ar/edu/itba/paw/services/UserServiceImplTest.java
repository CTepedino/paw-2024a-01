package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String USERNAME = "username";
    private static final long USER_ID = 1;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Test
    public void testFindByIdNonExisting(){
        // 1. Precondiciones
        Mockito.when(userDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // 2. Ejercita la class under test
        Optional<User> maybeUser = userService.findById(1);

        // 3. Postcondiciones - assertions
        Assert.assertNotNull(maybeUser);
        Assert.assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testFindByIdExistingUser(){
        // 1. Precondiciones
        Mockito.when(userDao.findById(Mockito.eq(USER_ID))).thenReturn(Optional.of(new User(USER_ID, USERNAME)));

        // 2. Ejercita la class under test
        Optional<User> maybeUser = userService.findById(USER_ID);

        // 3. Postcondiciones - assertions
        Assert.assertNotNull(maybeUser);
        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(USER_ID, maybeUser.get().getUserId());
    }

    @Test
    public void testCreate(){
        // 1. Precondiciones
        Mockito.when(userDao.create(Mockito.eq(USERNAME))).thenReturn(new User(USER_ID, USERNAME));

        // 2. Ejercita la class under test
        User user = userService.create(USERNAME);

        // 3. Postcondiciones - assertions
        Assert.assertNotNull(user);
        Assert.assertEquals(USERNAME, user.getUsername());
    }
}

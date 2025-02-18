package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJpaDaoTest {
    private static final String PASSWORD = "password";
    private static final String EMAIL = "johndoe@mail.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String INSERTED_EMAIL = "repeatedemail@error.com";
    private static final long EXISTING_ID = 102;
    private static final long NON_EXISTING_ID = 99999;

    private static final User TEST_USER = new User(EXISTING_ID, "", "","", "", true, Locale.US);

    @Autowired
    private UserJpaDao userDao;

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp(){
        TEST_USER.setRoles(new ArrayList<>());
    }

    @Test
    public void testCreateOK(){
        final User user = userDao.create(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, false, Locale.US);
        assertNotNull(user);
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());

        assertEquals(1, TestUtils.getRowCount(em, "FROM users u WHERE u.email = '" + EMAIL + "' AND u.password = '"+PASSWORD+"' AND u.first_name = '"+FIRST_NAME+"' AND u.last_name = '"+LAST_NAME+"'"));
    }

    @Test
    public void testCreateRepeatedEmail(){
        userDao.create(INSERTED_EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, false, Locale.US);

        assertThrows(
                PersistenceException.class,
                () -> em.flush()
        );
    }

    @Test
    public void testUpdate(){
        User user = em.find(User.class, EXISTING_ID);

        userDao.update(user, "NEW", "NAME", "myCbu", "desc");

        Assert.assertEquals("NEW", user.getFirstName());
        Assert.assertEquals("NAME", user.getLastName());
        Assert.assertEquals("myCbu", user.getCbu());
        Assert.assertEquals("desc", user.getDescription());
        Assert.assertEquals(1, TestUtils.getRowCount(em, "FROM users WHERE first_name = 'NEW' AND last_name = 'NAME' AND cbu = 'myCbu' AND description = 'desc'"));
    }


    @Test
    public void testFindByIdExisting(){
        Optional<User> maybeUser = userDao.findById(EXISTING_ID);

        assertNotNull(maybeUser);
        assertTrue(maybeUser.isPresent());
    }

    @Test
    public void testFindByIdNonExisting(){
        Optional<User> maybeUser = userDao.findById(NON_EXISTING_ID);

        assertNotNull(maybeUser);
        assertTrue(maybeUser.isEmpty());
    }

    @Test
    public void testFindByEmailExisting(){
        Optional<User> maybeUser = userDao.findByEmail(INSERTED_EMAIL);

        assertNotNull(maybeUser);
        assertTrue(maybeUser.isPresent());
    }

    @Test
    public void testFindByEmailNonExisting(){
        Optional<User> maybeUser = userDao.findByEmail(EMAIL);

        assertNotNull(maybeUser);
        assertTrue(maybeUser.isEmpty());
    }

    @Test
    public void testGiveRoleExistingUser(){
        userDao.giveRole(TEST_USER, UserRoles.WRITER);
        assertEquals(1, TestUtils.getRowCount(em, "FROM roles WHERE user_id = " + EXISTING_ID + " AND role = '" + UserRoles.WRITER + "'"));
    }


    @Test
    public void testCreateEmailValidation(){
        userDao.createEmailValidation(2, "12345", LocalDateTime.now().plusHours(12));

        assertEquals(1, TestUtils.getRowCount(em, "FROM email_validations WHERE id = 2"));
    }

    @Test
    public void testCreateEmailValidationAlreadyExists(){
        userDao.createEmailValidation(101, "12345", LocalDateTime.now().plusHours(12));

        assertThrows(
                PersistenceException.class,
                ()->em.flush()
        );
    }

    @Test
    public void testDeleteEmailValidation(){
        userDao.deleteEmailValidation(101);

        assertEquals(0, TestUtils.getRowCount(em, "FROM email_validations WHERE id=1"));
    }

    @Test
    public void testExpiredEmailValidation(){
        em.createNativeQuery("INSERT INTO email_validations VALUES (102, '12345', NOW() - INTERVAL '2' DAY)").executeUpdate();

        userDao.deleteExpiredEmailValidations();

        assertNull(em.find(User.class, 102L).getEmailValidation());
        assertEquals(0, TestUtils.getRowCount(em, "FROM email_validations WHERE id=102"));
    }

    @Test
    public void testNonExpiredEmailValidation(){
        em.createNativeQuery("INSERT INTO email_validations VALUES (102, '12345', NOW() + INTERVAL '1' hour)").executeUpdate();

        userDao.deleteExpiredEmailValidations();

        assertNotNull(em.find(User.class, 102L).getEmailValidation());
        assertEquals(1, TestUtils.getRowCount(em, "FROM email_validations WHERE id=102"));
    }

    @Test
    public void testCreateResetCode(){
        userDao.createResetCode(2, "12345", LocalDateTime.now().plusHours(12));

        assertEquals(1, TestUtils.getRowCount(em, "FROM reset_codes WHERE id = 2"));
    }

    @Test
    public void testCreateResetCodeAlreadyExists(){
        userDao.createResetCode(101, "12345", LocalDateTime.now().plusHours(12));

        assertThrows(
                PersistenceException.class,
                ()->em.flush()
        );
    }

    @Test
    public void testDeleteResetCode(){
        userDao.deleteResetCode(101);

        assertEquals(0, TestUtils.getRowCount(em, "FROM reset_codes WHERE id=1"));
    }

    @Test
    public void testExpiredResetCode(){
        em.createNativeQuery("INSERT INTO reset_codes VALUES (102, '12345', NOW() - INTERVAL '2' DAY)").executeUpdate();

        userDao.deleteExpiredResetCodes();

        assertNull(em.find(User.class, 102L).getResetCode());
        assertEquals(0, TestUtils.getRowCount(em, "FROM reset_codes WHERE id=102"));
    }

    @Test
    public void testNonExpiredResetCode(){
        em.createNativeQuery("INSERT INTO reset_codes VALUES (102, '12345', NOW() + INTERVAL '1' hour)").executeUpdate();

        userDao.deleteExpiredResetCodes();

        assertNotNull(em.find(User.class, 102L).getResetCode());
        assertEquals(1, TestUtils.getRowCount(em, "FROM reset_codes WHERE id=102"));
    }
}
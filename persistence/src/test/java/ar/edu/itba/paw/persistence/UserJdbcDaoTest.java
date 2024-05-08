package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
    private static final String PASSWORD = "password";
    private static final String EMAIL = "johnDoe@mail.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String INSERTED_EMAIL = "repeatedEmail@error.com";
    private static final long EXISTING_ID = 1;
    private static final long NON_EXISTING_ID = 99999;

    @Autowired
    private DataSource ds;

    @Autowired
    private UserJdbcDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreateOK(){
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

        final User user = userDao.create(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, false);

        assertNotNull(user);
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(rows+1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test
    public void testCreateRepeatedEmail(){
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

        assertThrows(
                DuplicateKeyException.class,
                () -> userDao.create(INSERTED_EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, false)
        );
        assertEquals(rows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

    }

    @Test
    public void testUpdateExisting(){
        int updatedRows = userDao.update(EXISTING_ID,"","","", "", false);

        assertEquals(1, updatedRows);
    }

    @Test
    public void testUpdateNonExisting(){
        int updatedRows = userDao.update(NON_EXISTING_ID, "", "", "", "", false);

        assertEquals(0, updatedRows);
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
        userDao.giveRole(EXISTING_ID, UserRoles.WRITER);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "roles", "user_id = " + EXISTING_ID + " AND role = '" + UserRoles.WRITER + "'"));
    }

    @Test
    public void testGiveRoleNonExistingUser(){
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "roles");

        assertThrows(
                DataIntegrityViolationException.class,
                () -> userDao.giveRole(NON_EXISTING_ID, UserRoles.READER)
        );

        assertEquals(rows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "roles"));
    }

    @Test
    public void testGetRoles(){
        List<UserRoles> roles = userDao.getRoles(EXISTING_ID);

        assertNotNull(roles);
        assertArrayEquals(new UserRoles[] {UserRoles.READER}, roles.toArray());
    }
}
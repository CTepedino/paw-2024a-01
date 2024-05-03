package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Optional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
   private static final String EMAIL = "test@email.com";
   private static final String FIRST_NAME = "john";
   private static final String LAST_NAME = "doe";
   private static final String PASSWORD = "password123";

    @Autowired
    private DataSource ds;

    @Autowired
    private UserJdbcDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }
/*
    @Test
    public void testCreate(){
        int prevRowCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

        User user = userDao.create(new UserRoles[]{UserRoles.READER}, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);

        Assert.assertNotNull(user);
        Assert.assertEquals(prevRowCount+1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
        Assert.assertEquals(EMAIL, user.getEmail());
        Assert.assertEquals(FIRST_NAME, user.getFirstName());
        Assert.assertEquals(LAST_NAME, user.getLastName());
        Assert.assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testFindByIdExisting(){
        Optional<User> user = userDao.findById(1);

        Assert.assertNotNull(user);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(1, user.get().getUserId());
    }

    @Test
    public void testFindByIdNonExisting(){
        Optional<User> user = userDao.findById(100);

        Assert.assertNotNull(user);
        Assert.assertTrue(user.isEmpty());
    }

    @Test
    public void testGiveRoleExisting(){
        User user = userDao.giveRole(1, UserRoles.WRITER);

        Assert.assertNotNull(user);
        Assert.assertTrue(Arrays.asList(user.getRoles()).contains(UserRoles.WRITER));
    }

    @Test
    public void testGiveRoleToNonExisting(){
        Assert.assertThrows(
                UserNotFoundException.class,
                () -> userDao.giveRole(100, UserRoles.WRITER)
        );

    }*/
}

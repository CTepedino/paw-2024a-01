package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.users.EmailValidation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class EmailValidationJdbcDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private EmailValidationJdbcDao evDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }


    @Test
    public void testCreate(){
        evDao.create(2, "12345", LocalDateTime.now().plusHours(12));

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "email_validations", "id = 1"));
    }

    @Test
    public void testCreateNonExistingUserId(){
        assertThrows(
                DataIntegrityViolationException.class,
                ()->evDao.create(999999, "12345", LocalDateTime.now().plusHours(12))
        );
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "email_validations", "id = 999999"));
    }

    @Test
    public void testCreateAlreadyExists(){
        assertThrows(
                DataIntegrityViolationException.class,
                ()->evDao.create(1, "12345", LocalDateTime.now().plusHours(12))
        );
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "email_validations", "id = 1"));
    }


    @Test
    public void testGetExisting(){
        Optional<EmailValidation> maybeValidation = evDao.get(1);

        assertNotNull(maybeValidation);
        assertTrue(maybeValidation.isPresent());
    }

    @Test
    public void testGetNonExisting(){
        Optional<EmailValidation> maybeValidation = evDao.get(99999);

        assertNotNull(maybeValidation);
        assertTrue(maybeValidation.isEmpty());
    }

    @Test
    public void testDelete(){
        evDao.delete(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "email_validations", "id=1"));
    }

    @Test
    public void testExpired(){
        jdbcTemplate.execute("INSERT INTO email_validations VALUES (2, '12345', NOW())");

        evDao.deleteExpired();

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "email_validations", "id=2"));
    }

    @Test
    public void testNonExpired(){
        jdbcTemplate.execute("INSERT INTO email_validations VALUES (2, '12345', NOW() + INTERVAL '1' hour)");

        evDao.deleteExpired();

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "email_validations", "id=2"));
    }
}

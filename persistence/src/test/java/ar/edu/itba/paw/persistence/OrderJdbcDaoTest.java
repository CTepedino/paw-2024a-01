package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.orders.OrderStatus;
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

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class OrderJdbcDaoTest{


    @Autowired
    private DataSource ds;

    @Autowired
    private OrderJdbcDao orderDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreateOK(){
        orderDao.create(1, 2, OrderStatus.WAITING_CONTACT);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "orders", "buyer_id = 1 AND book_id = 2"));
    }

    @Test
    public void testCreateAlreadyExists(){
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");
        assertThrows(
                DuplicateKeyException.class,
                () -> orderDao.create(1, 1, OrderStatus.WAITING_CONTACT)
        );
        assertEquals(rows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders"));
    }

    @Test
    public void testCreateNonExistingBuyer(){
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");
        assertThrows(
                DataIntegrityViolationException.class,
                () -> orderDao.create(99999, 1, OrderStatus.WAITING_CONTACT)
        );
        assertEquals(rows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders"));
    }

    @Test
    public void testCreateNonExistingBook(){
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");
        assertThrows(
                DataIntegrityViolationException.class,
                () -> orderDao.create(1, 99999, OrderStatus.WAITING_CONTACT)
        );
        assertEquals(rows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders"));
    }
}

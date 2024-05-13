package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.UserRoles;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class OrderJdbcDaoTest{


    private static final long NON_EXISTING_BUYER_ID = 99999;
    private static final long EXISTING_BUYER_ID = 1;

    private static final long EXISTING_BOOK_ID = 2;
    private static final long NON_EXISTING_BOOK_ID = 9999;

    private static final long EXISTING_ORDER_ID = 1;
    private static final long NON_EXISTING_ORDER_ID = 9999;

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
        long orderId = orderDao.create(EXISTING_BUYER_ID, EXISTING_BOOK_ID, OrderStatus.WAITING_CONTACT);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "orders", "order_id = " + orderId));
    }

    @Test
    public void testCreateNonExistingBuyer(){
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");
        assertThrows(
                DataIntegrityViolationException.class,
                () -> orderDao.create(NON_EXISTING_BUYER_ID, EXISTING_BOOK_ID, OrderStatus.WAITING_CONTACT)
        );
        assertEquals(rows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders"));
    }

    @Test
    public void testCreateNonExistingBook(){
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");
        assertThrows(
                DataIntegrityViolationException.class,
                () -> orderDao.create(EXISTING_BUYER_ID, NON_EXISTING_BOOK_ID, OrderStatus.WAITING_CONTACT)
        );
        assertEquals(rows, JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders"));
    }

    @Test
    public void testFindByIdExisting(){
        Optional<Order> maybeOrder = orderDao.findById(EXISTING_ORDER_ID);

        assertNotNull(maybeOrder);
        assertTrue(maybeOrder.isPresent());
    }

    @Test
    public void testFindByIdNonExisting(){
        Optional<Order> maybeOrder = orderDao.findById(NON_EXISTING_ORDER_ID);

        assertNotNull(maybeOrder);
        assertTrue(maybeOrder.isEmpty());
    }

    @Test
    public void testFindExisting(){
        Optional<Order> maybeOrder = orderDao.find(EXISTING_BUYER_ID, EXISTING_BOOK_ID);

        assertNotNull(maybeOrder);
        assertTrue(maybeOrder.isPresent());
    }

    @Test
    public void testFindNonExisting(){
        Optional<Order> maybeOrder = orderDao.find(NON_EXISTING_BUYER_ID, NON_EXISTING_BOOK_ID);

        assertNotNull(maybeOrder);
        assertTrue(maybeOrder.isEmpty());
    }

    @Test
    public void testSetStatusExistingOrder(){
        orderDao.update(EXISTING_ORDER_ID, OrderStatus.WAITING_PAYMENT);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "orders", "order_id = " + EXISTING_ORDER_ID + " AND status = '" + OrderStatus.WAITING_PAYMENT + "'"));
    }

    @Test
    public void testSetStatusNonExistingOrder(){
        int rowsBeforeUpdate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");

        orderDao.update(NON_EXISTING_ORDER_ID, OrderStatus.WAITING_PAYMENT);

        int rowsAfterUpdate = JdbcTestUtils.countRowsInTable(jdbcTemplate, "orders");

        assertEquals(rowsBeforeUpdate, rowsAfterUpdate);
    }

    @Test
    public void testAllReaderOrders(){
        List<Order> orders = orderDao.getReaderOrders(1, "", null, 0, 999);

        assertNotNull(orders);
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "orders", "buyer_id = 1"), orders.size());
    }

    @Test
    public void testGetWriterOrders(){
        List<Order> orders = orderDao.getWriterOrders(2, "", null, 0, 999);

        assertNotNull(orders);
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "orders o JOIN books b ON o.book_id = b.book_id", "b.writer_id = 2"), orders.size());
    }

    @Test
    public void testGetWriterOrdersWithParams(){
        List<Order> orders = orderDao.getWriterOrders(2, "my book", OrderStatus.WAITING_CONTACT, 0, 999);

        assertNotNull(orders);
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "orders o JOIN books b ON o.book_id = b.book_id", "b.title LIKE '%my book%' AND o.status = 'WAITING_CONTACT' AND b.writer_id = 2"), orders.size());
    }

    @Test
    public void testOwnsBook(){
        boolean owns = orderDao.ownsBook(1, "booksPaused@mail.com");

        assertTrue(owns);
    }

    @Test
    public void testNotOwnsBook(){
        boolean owns = orderDao.ownsBook(1,"anotherMail@mail.com");

        assertFalse(owns);
    }


}

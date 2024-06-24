package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class OrderJpaDaoTest{

    private static final long NON_EXISTING_ORDER_ID = 999;
    private static final long NON_EXISTING_BUYER_ID = 999;
    private static final long NON_EXISTING_BOOK_ID = 999;

    private static final long EXISTING_BUYER_ID = 101;
    private static final User TEST_USER = new User(EXISTING_BUYER_ID, "", "", "", "", false, Locale.US);

    private static final long EXISTING_BOOK_ID = 101;
    private static final Book TEST_BOOK = new Book(EXISTING_BOOK_ID, "", "", BookGenre.BIOGRAPHY, BigDecimal.ONE, 1,1 ,LocalDate.now(), TEST_USER, false);

    private static final long EXISTING_ORDER_ID = 101;
    private static final long EXISTING_ORDER_BUYER_ID = 102;
    private static final long EXISTING_ORDER_BOOK_ID = 103;

    @Autowired
    private OrderJpaDao orderDao;

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp(){
    }

    @Test
    public void testCreateOK(){
        Order order = orderDao.create(TEST_USER, TEST_BOOK, OrderStatus.WAITING_APPROVAL, LocalDateTime.now(), true, TEST_BOOK.getPrice());

        assertEquals(1, TestUtils.getRowCount(em, "FROM orders WHERE order_id = " + order.getOrderId()));
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
        Optional<Order> maybeOrder = orderDao.find(EXISTING_ORDER_BUYER_ID, EXISTING_ORDER_BOOK_ID);

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
        Order order = em.find(Order.class, EXISTING_ORDER_ID);

        orderDao.update(order, OrderStatus.WAITING_APPROVAL, LocalDateTime.now(), true);

        assertEquals(1, TestUtils.getRowCount(em, "FROM orders WHERE order_id = " + EXISTING_ORDER_ID + " AND status = '" + OrderStatus.WAITING_APPROVAL + "'"));
    }

    @Test
    public void testAllReaderOrders(){
        List<Order> orders = orderDao.getReaderOrders(1, "", null, 0, 999);

        assertNotNull(orders);
        assertEquals(TestUtils.getRowCount(em, "FROM orders WHERE buyer_id = 1"), orders.size());
    }

    @Test
    public void testGetWriterOrders(){
        List<Order> orders = orderDao.getWriterOrders(2, "", null, 0, 999);

        assertNotNull(orders);
        assertEquals(TestUtils.getRowCount(em, "FROM orders o JOIN books b ON o.book_id = b.book_id WHERE b.writer_id = 2"), orders.size());
    }

    @Test
    public void testGetWriterOrdersWithParams(){
        List<Order> orders = orderDao.getWriterOrders(2, "my book", OrderStatus.WAITING_APPROVAL, 0, 999);

        assertNotNull(orders);
        assertEquals(TestUtils.getRowCount(em, "FROM orders o JOIN books b ON o.book_id = b.book_id WHERE b.title LIKE '%my book%' AND o.status = 'WAITING_APPROVAL' AND b.writer_id = 2"), orders.size());
    }

    @Test
    public void testOwnsBook(){
        boolean owns = orderDao.ownsBook(102, "booksPaused@mail.com");

        assertTrue(owns);
    }

    @Test
    public void testNotOwnsBook(){
        boolean owns = orderDao.ownsBook(101,"anotherMail@mail.com");

        assertFalse(owns);
    }
}
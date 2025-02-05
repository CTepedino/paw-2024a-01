package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.exception.InvalidOrderUpdateException;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {


    private static final User TEST_WRITER = new User(1, "", "", "", "", true, Locale.US);
    private static final User TEST_READER = new User(2, "", "", "", "", true, Locale.US);
    private static final Book PAUSED_BOOK = new Book(1, "", "", BookGenre.BIOGRAPHY, BigDecimal.ONE, 0, 0, null, TEST_WRITER, true);
    private static final Book UNPAUSED_BOOK = new Book(1, "", "", BookGenre.BIOGRAPHY, BigDecimal.ONE, 0, 0, null, TEST_WRITER, false);


    @Mock
    private OrderDao orderDao;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;


    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void testUpdateOrderWriterSideIllegalStatus(){
        Mockito.when(orderDao.findById(Mockito.anyLong())).thenReturn(Optional.of(new Order(TEST_WRITER,null, OrderStatus.COMPLETED, null , UNPAUSED_BOOK.getPrice())));

        assertThrows(
                InvalidOrderUpdateException.class,
                () -> orderService.updateOrderWriterSide(1, "")
        );
    }

    @Test
    public void testUpdateOrderBuyerSideIllegalStatus(){
        Mockito.when(orderDao.findById(Mockito.anyLong())).thenReturn(Optional.of(new Order(TEST_WRITER, null, OrderStatus.WAITING_APPROVAL, null, UNPAUSED_BOOK.getPrice())));

        assertThrows(
                InvalidOrderUpdateException.class,
                () -> orderService.updateOrderBuyerSide(1, null, null)
        );
    }
}
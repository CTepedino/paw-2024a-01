package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class PublishServiceImplTest {

    private static final long USER_ID = 1;
    private static final long BOOK_ID = 1;

    private static final byte[] PREVIEW = new byte[100];
    private static final byte[] COVER = new byte[100];
    private static final byte[] BOOK_FILE = new byte[100];

    private static final User TEST_WRITER = new User("", "", "", "", false, Locale.US);

    private static final List<UserRoles> MOCKED_ROLES = new ArrayList<>();

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;
/*
    @InjectMocks
    private PublishServiceImpl publishService;

    @Before
    public void setup(){
        Mockito.when(bookService.create(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any(BigDecimal.class),
                Mockito.anyInt(),
                Mockito.anyInt(),
                Mockito.any(),
                Mockito.any(User.class),
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(BOOK_ID);
        TEST_WRITER.setRoles(MOCKED_ROLES);
    }

    @Test
    public void testPublishNotWriter(){
        MOCKED_ROLES.clear();
        MOCKED_ROLES.add(UserRoles.READER);

        long bookId = publishService.publishBook(
                TEST_WRITER,
                "",
                "",
                "",
                BookGenre.FICTION,
                1,
                new BigDecimal(1),
                1,
                LocalDate.now(),
                COVER,
                PREVIEW,
                BOOK_FILE
        );

        Assert.assertEquals(BOOK_ID, bookId);
    }

    @Test
    public void testPublishAsWriter(){
        MOCKED_ROLES.clear();
        MOCKED_ROLES.add(UserRoles.READER);
        MOCKED_ROLES.add(UserRoles.WRITER);


        long bookId = publishService.publishBook(
                TEST_WRITER,
                "",
                "",
                "",
                BookGenre.FICTION,
                1,
                new BigDecimal(1),
                1,
                LocalDate.now(),
                COVER,
                PREVIEW,
                BOOK_FILE
        );

        Assert.assertEquals(BOOK_ID, bookId);
        Assert.assertEquals(MOCKED_ROLES.indexOf(UserRoles.WRITER), MOCKED_ROLES.lastIndexOf(UserRoles.WRITER));
    }*/
}
package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PublishServiceImplTest {

    private static final long USER_ID = 1;
    private static final long BOOK_ID = 1;
    private static final String USER_CBU = "1234567891234567891234";

    private static final MultipartFile PREVIEW = new MockMultipartFile("testPreview", new byte[100]);
    private static final MultipartFile COVER = new MockMultipartFile("testCover", new byte[100]);
    private static final MultipartFile BOOK_FILE = new MockMultipartFile("testBook", new byte[100]);

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PublishServiceImpl publishService;

    private final List<UserRoles> mockedRoles = new ArrayList<>();

    @Before
    public void setup(){
        Mockito.when(userService.getLoggedUser())
                .thenReturn(Optional.of(new User(USER_ID, "", "", "", "",false)));
        Mockito.when(userService.getRoles(Mockito.eq(USER_ID))).thenReturn(mockedRoles);
        Mockito.doAnswer((Answer<Void>) invocation ->{
            mockedRoles.add(UserRoles.WRITER);
            return null;
        }).when(userService).giveWriterRole(Mockito.eq(USER_ID), Mockito.eq(USER_CBU));
        Mockito.when(bookService.create(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any(BigDecimal.class),
                Mockito.anyInt(),
                Mockito.anyInt(),
                Mockito.anyLong(),
                Mockito.any(MultipartFile.class),
                Mockito.any(MultipartFile.class),
                Mockito.any(MultipartFile.class)
        )).thenReturn(BOOK_ID);
    }

    @Test
    public void testPublishNotWriter(){
        mockedRoles.clear();
        mockedRoles.add(UserRoles.READER);

        long bookId = publishService.publishBook(
                "",
                "",
                "",
                BookGenre.FICTION,
                1,
                new BigDecimal(1),
                1,
                COVER,
                PREVIEW,
                BOOK_FILE
        );

        assertEquals(BOOK_ID, bookId);
    }

    @Test
    public void testPublishAsWriter(){
        mockedRoles.clear();
        mockedRoles.add(UserRoles.READER);
        mockedRoles.add(UserRoles.WRITER);


        long bookId = publishService.publishBook(
                "",
                "",
                "",
                BookGenre.FICTION,
                1,
                new BigDecimal(1),
                1,
                COVER,
                PREVIEW,
                BOOK_FILE
        );

        assertEquals(BOOK_ID, bookId);
        assertEquals(mockedRoles.indexOf(UserRoles.WRITER), mockedRoles.lastIndexOf(UserRoles.WRITER));
    }
}

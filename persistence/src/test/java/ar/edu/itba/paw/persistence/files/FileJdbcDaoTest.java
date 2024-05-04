package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.models.files.BookPreview;
import ar.edu.itba.paw.models.files.File;
import ar.edu.itba.paw.persistence.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class FileJdbcDaoTest {
    //La lógica de todos los DAO que manejan tablas de archivos es análoga, y todos extienden a un FileJdbcDao abstracto. Debido a eso, para los tests solo se usa el BookPreviewJdbcDao

    private static final byte[] FILE = new byte[100];
    private static final long EXISTING_ID = 1;
    private static final long NON_EXISTING_ID = 99999;

    @Autowired
    private DataSource ds;

    @Autowired
    private BookPreviewJdbcDao previewDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }


    @Test
    public void testFindByIdExisting(){
        Optional<BookPreview> maybePreview = previewDao.findById(EXISTING_ID);

        assertNotNull(maybePreview);
        assertTrue(maybePreview.isPresent());
    }

    @Test
    public void testFindByIdNonExisting(){
        Optional<BookPreview> maybePreview = previewDao.findById(NON_EXISTING_ID);

        assertNotNull(maybePreview);
        assertTrue(maybePreview.isEmpty());
    }

    @Test
    public void testCreate(){
        long id = previewDao.create(FILE);

        assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book_previews", "id = " + id));
    }
}

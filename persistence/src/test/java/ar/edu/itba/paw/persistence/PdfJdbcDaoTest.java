package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Pdf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PdfJdbcDaoTest {

    private static final byte[] PDF = {0};

    @Autowired
    private DataSource ds;

    @Autowired
    private PdfJdbcDao pdfDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "pdfs");
    }

    @Test
    public void testCreate(){
        final Pdf pdf = pdfDao.create(PDF);

        Assert.assertNotNull(pdf);
        Assert.assertEquals(PDF, pdf.getPdf());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "pdfs"));
    }

}

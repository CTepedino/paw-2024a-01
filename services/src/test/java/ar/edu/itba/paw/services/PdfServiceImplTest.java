package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.PdfDao;
import ar.edu.itba.paw.models.Pdf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class PdfServiceImplTest {

    private static final long PDF_ID = 1;
    private static final MockMultipartFile MOCK_PDF = new MockMultipartFile("test.pdf", "test.pdf", "application/pdf", "a pdf".getBytes());

    @InjectMocks
    private PdfServiceImpl pdfService;

    @Mock
    private PdfDao pdfDao;


    @Test
    public void testFindByIdNonExisting(){
        Mockito.when(pdfDao.findById(Mockito.eq(PDF_ID))).thenReturn(Optional.empty());

        Optional<Pdf> maybePdf = pdfService.findById(PDF_ID);

        Assert.assertNotNull(maybePdf);
        Assert.assertFalse(maybePdf.isPresent());
    }

    @Test
    public void testFindByIdExisting(){
        Mockito.when(pdfDao.findById(Mockito.eq(PDF_ID))).thenReturn(Optional.of(new Pdf(PDF_ID, new byte[1])));

        Optional<Pdf> maybePdf = pdfService.findById(PDF_ID);

        Assert.assertNotNull(maybePdf);
        Assert.assertTrue(maybePdf.isPresent());
        Assert.assertEquals(PDF_ID, maybePdf.get().getPdfId());
    }

    @Test
    public void testCreate(){
        try {
            Mockito.when(pdfDao.create(Mockito.eq(MOCK_PDF.getBytes()))).thenReturn(new Pdf(PDF_ID, MOCK_PDF.getBytes()));

            Pdf pdf = pdfService.create(MOCK_PDF);

            Assert.assertNotNull(pdf);
            Assert.assertEquals(MOCK_PDF.getBytes(), pdf.getPdf());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

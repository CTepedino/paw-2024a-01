package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.models.Image;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

    private static final long IMAGE_ID = 1;
    private static final MockMultipartFile MOCK_IMAGE = new MockMultipartFile("test.png", "test.png", "image/png", "an image".getBytes());

    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private ImageDao imageDao;

    @Test
    public void testFindByIdNonExisting(){
        Mockito.when(imageDao.findById(Mockito.eq(IMAGE_ID))).thenReturn(Optional.empty());

        Optional<Image> maybeImage = imageService.findById(IMAGE_ID);

        Assert.assertNotNull(maybeImage);
        Assert.assertFalse(maybeImage.isPresent());
    }

    @Test
    public void testFindByIdExisting(){
        Mockito.when(imageDao.findById(Mockito.eq(IMAGE_ID))).thenReturn(Optional.of(new Image(IMAGE_ID, new byte[1])));

        Optional<Image> maybeImage = imageService.findById(IMAGE_ID);

        Assert.assertNotNull(maybeImage);
        Assert.assertTrue(maybeImage.isPresent());
        Assert.assertEquals(IMAGE_ID, maybeImage.get().getImageId());
    }

    @Test
    public void testCreate(){
        try {
            Mockito.when(imageDao.create(Mockito.eq(MOCK_IMAGE.getBytes()))).thenReturn(new Image(IMAGE_ID, MOCK_IMAGE.getBytes()));

            Image image = imageService.create(MOCK_IMAGE);

            Assert.assertNotNull(image);
            Assert.assertEquals(MOCK_IMAGE.getBytes(), image.getImage());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

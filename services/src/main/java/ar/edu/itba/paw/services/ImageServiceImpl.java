package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.exceptions.ImageConvException;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
@Service

public class ImageServiceImpl implements ImageService {

    private final ImageDao imageDao;
    @Autowired
    public ImageServiceImpl(final ImageDao imageDao){
        this.imageDao = imageDao;

    }

    @Override
    public Optional<Image> findbyid(long image_id) {
        return imageDao.findbyid(image_id);
    }

    @Override
    public Optional<Image> findbyid(String imagePath) throws URISyntaxException, IOException, ImageConvException {
        try {
            final byte[] imageBytes = Files.readAllBytes(Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource(imagePath)).toURI()));
            final Image image = new Image(0, imageBytes);
            return Optional.of(image);
        } catch (URISyntaxException | IOException e) {
            throw new ImageConvException();
        }
    }

    @Override
    public Image uploadImage(byte[] photoBlob) {
        return imageDao.uploadImage(photoBlob);
    }

    @Override
    public void deleteImage(long image_id) {
        imageDao.deleteImage(image_id);
    }
}

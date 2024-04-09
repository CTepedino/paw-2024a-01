package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.interfaces.exceptions.ImageConvException;
import ar.edu.itba.paw.models.Image;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public interface ImageService {
    Optional<Image> findbyid(long image_id);

    Optional<Image> findbyid(String imagePath) throws URISyntaxException, IOException, ImageConvException;

    Image uploadImage(byte[] photoBlob);

    void deleteImage(long image_id);
}
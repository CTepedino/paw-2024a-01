package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.models.Image;

import java.util.Optional;

public interface ImageDao {
        Optional<Image> findbyid(long image_id);

        Image uploadImage(byte[] photoBlob);

        void deleteImage(long image_id);
}


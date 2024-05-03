package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Image;

import java.io.File;
import java.util.Optional;

public interface ImageDao {
    Optional<Image> findById(long id);

    Image create(byte[] image);
}

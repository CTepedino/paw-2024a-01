package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ImageService {
    Optional<Image> findById(long id);

    Image create(MultipartFile image);

}

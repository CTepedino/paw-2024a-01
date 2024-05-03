package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {


    private final ImageDao imageDao;


    @Autowired
    public ImageServiceImpl(ImageDao imageDao){
        this.imageDao = imageDao;
    }
    @Transactional(readOnly = true)
    @Override
    public Optional<Image> findById(long id) {
        return imageDao.findById(id);
    }

    @Transactional
    @Override
    public Image create(MultipartFile image) {
        try {
            return imageDao.create(image.getBytes());
        } catch (IOException e){
            return null;
        }
    }
}

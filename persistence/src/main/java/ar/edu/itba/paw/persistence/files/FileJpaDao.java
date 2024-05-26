package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.FileDao;
import ar.edu.itba.paw.models.files.File;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public abstract class FileJpaDao<T extends File> implements FileDao<T> {


    @Override
    public Optional<T> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void update(long id, byte[] file) {

    }

    @Override
    public long create(long id, byte[] file) {
        return 0;
    }
}

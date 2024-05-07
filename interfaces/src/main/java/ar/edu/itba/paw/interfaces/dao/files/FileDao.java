package ar.edu.itba.paw.interfaces.dao.files;

import ar.edu.itba.paw.models.files.File;

import java.util.Optional;

public interface FileDao<F extends File> {

    Optional<F> findById(long id);

    void update(long id, byte[] file);

    long create(long id, byte[] file);
}

package ar.edu.itba.paw.interfaces.dao.files;

import ar.edu.itba.paw.models.files.File;

import java.util.Optional;

public interface FileDao<F extends File> {

    Optional<F> findById(long id);

    long create(byte[] file);
}

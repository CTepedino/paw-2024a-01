package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Pdf;

import java.util.Optional;

public interface PdfDao {
    Optional<Pdf> findById(long id);

    Pdf create(byte[] pdf);
}

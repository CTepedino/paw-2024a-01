package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Writer;

import java.util.Optional;

public interface WriterDao {
    Optional<Writer> findById(long id);

    Writer create(String name, String lastName, String email);
}

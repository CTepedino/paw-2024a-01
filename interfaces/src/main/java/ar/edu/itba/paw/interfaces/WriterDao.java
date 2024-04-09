package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Writer;
import java.util.Optional;

public interface WriterDao {
    Optional<Writer> findById(long id);

    Optional<Writer> findByEmail(String email);

    Writer create(String name, String lastName, String email);
}
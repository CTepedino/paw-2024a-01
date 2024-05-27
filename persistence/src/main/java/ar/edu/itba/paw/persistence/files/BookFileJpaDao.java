package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.BookFileDao;
import ar.edu.itba.paw.models.files.BookFile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class BookFileJpaDao implements BookFileDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<BookFile> findById(long id) {
        return Optional.ofNullable(em.find(BookFile.class, id));
    }

    @Override
    public void update(long id, byte[] file) {
        findById(id).ifPresent(book -> {
            book.setFile(file);
            em.merge(book);
        });
    }

    @Override
    public long create(long id, byte[] file) {
        BookFile book = new BookFile(id, file);
        em.persist(book);
        return id;
    }
}

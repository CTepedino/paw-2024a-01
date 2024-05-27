package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.BookPreviewDao;
import ar.edu.itba.paw.models.files.BookPreview;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class BookPreviewJpaDao implements BookPreviewDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<BookPreview> findById(long id) {
        return Optional.ofNullable(em.find(BookPreview.class, id));
    }

    @Override
    public void update(long id, byte[] file) {
        findById(id).ifPresent(preview -> {
            preview.setFile(file);
            em.merge(preview);
        });
    }

    @Override
    public long create(long id, byte[] file) {
        BookPreview preview = new BookPreview(id, file);
        em.persist(preview);
        return id;
    }
}

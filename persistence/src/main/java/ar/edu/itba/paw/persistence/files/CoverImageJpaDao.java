package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.CoverImageDao;
import ar.edu.itba.paw.models.files.CoverImage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class CoverImageJpaDao implements CoverImageDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<CoverImage> findById(long id) {
        return Optional.ofNullable(em.find(CoverImage.class, id));
    }

    @Override
    public void update(long id, byte[] file) {
        findById(id).ifPresent(cover -> {
            cover.setFile(file);
            em.merge(cover);
        });
    }

    @Override
    public long create(long id, byte[] file) {
        CoverImage coverImage = new CoverImage(id, file);
        em.persist(coverImage);
        return id;
    }
}

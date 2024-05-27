package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.ProfilePictureDao;
import ar.edu.itba.paw.models.files.ProfilePicture;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class ProfilePictureJpaDao implements ProfilePictureDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createOrUpdate(long id, byte[] file) {
        if (findById(id).isPresent()) {
            update(id, file);
        } else {
            create(id, file);
        }
    }

    @Override
    public Optional<ProfilePicture> findById(long id) {
        return Optional.ofNullable(em.find(ProfilePicture.class, id));
    }

    @Override
    public void update(long id, byte[] file) {
        findById(id).ifPresent(picture -> {
            picture.setFile(file);
            em.merge(picture);
        });
    }

    @Override
    public long create(long id, byte[] file) {
        ProfilePicture profilePicture = new ProfilePicture(id, file);
        em.persist(profilePicture);
        return id;
    }
}

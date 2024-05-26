package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.ProfilePictureDao;
import ar.edu.itba.paw.models.files.ProfilePicture;
import org.springframework.stereotype.Repository;

@Repository
public class ProfilePictureJpaDao extends FileJpaDao<ProfilePicture> implements ProfilePictureDao {

    @Override
    public void createOrUpdate(long id, byte[] file) {

    }
}

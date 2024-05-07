package ar.edu.itba.paw.interfaces.dao.files;

import ar.edu.itba.paw.models.files.ProfilePicture;

public interface ProfilePictureDao extends FileDao<ProfilePicture> {

    void createOrUpdate(long id, byte[] file);
}

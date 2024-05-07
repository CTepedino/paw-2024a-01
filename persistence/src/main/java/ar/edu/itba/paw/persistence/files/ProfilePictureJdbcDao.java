package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.ProfilePictureDao;
import ar.edu.itba.paw.models.files.ProfilePicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ProfilePictureJdbcDao extends FileJdbcDao<ProfilePicture> implements ProfilePictureDao {

    private static final RowMapper<ProfilePicture> ROW_MAPPER = (rs, rowNum) -> new ProfilePicture(rs.getLong("id"), rs.getBytes("file"));

    @Autowired
    public ProfilePictureJdbcDao(DataSource ds) {
        super(ds, "profile_pictures", ROW_MAPPER);
    }

    @Override
    public void createOrUpdate(long id, byte[] file) {
        if (super.findById(id).isPresent()){
            super.update(id, file);
        } else {
            super.create(id, file);
        }
    }
}

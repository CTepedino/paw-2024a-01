package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.BookPreviewDao;
import ar.edu.itba.paw.interfaces.dao.files.CoverImageDao;
import ar.edu.itba.paw.models.files.CoverImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CoverImageJdbcDao extends FileJdbcDao<CoverImage> implements CoverImageDao {

    private static final RowMapper<CoverImage> ROW_MAPPER = (rs, rowNum) -> new CoverImage(rs.getLong("id"), rs.getBytes("file"));

    @Autowired
    CoverImageJdbcDao(DataSource ds) {
        super(ds, "cover_images", ROW_MAPPER);
    }

}

package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.BookPreviewDao;
import ar.edu.itba.paw.models.files.BookPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class BookPreviewJdbcDao extends FileJdbcDao<BookPreview> implements BookPreviewDao {

    private static final RowMapper<BookPreview> ROW_MAPPER = (rs, rowNum) -> new BookPreview(rs.getLong("id"), rs.getBytes("file"));

    @Autowired
    public BookPreviewJdbcDao(final DataSource ds){
        super(ds, "book_previews", ROW_MAPPER);
    }

}

package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.BookFileDao;
import ar.edu.itba.paw.models.files.BookFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class BookFileJdbcDao extends FileJdbcDao<BookFile> implements BookFileDao {

    private static final RowMapper<BookFile> ROW_MAPPER = (rs, rowNum) -> new BookFile(rs.getLong("id"), rs.getBytes("file"));

    @Autowired
    public BookFileJdbcDao(DataSource ds) {
        super(ds, "book_files", ROW_MAPPER);
    }

}

package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.FileDao;
import ar.edu.itba.paw.models.files.File;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class FileJdbcDao<F extends File> implements FileDao<F> {

    private final RowMapper<F> rowMapper;
    private final String tableName;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    FileJdbcDao(final DataSource ds, final String tableName, final RowMapper<F> rowMapper){
        this.tableName = tableName;
        this.rowMapper = rowMapper;
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(tableName);
    }

    @Override
    public Optional<F> findById(long id) {
        final List<F> list = jdbcTemplate.query(
            "SELECT * FROM " +
                tableName +
                " WHERE id = ?",
                rowMapper,
                id
        );
        return list.stream().findFirst();
    }

    @Override
    public void update(long id, byte[] file){
        jdbcTemplate.update(
            "UPDATE " + tableName +
                " SET file = ? " +
                "WHERE id = ?",
                file,
                id
        );
    }

    @Override
    public long create(long id, byte[] file) {
        Map<String, Object> fileData = new HashMap<>();
        fileData.put("id", id);
        fileData.put("file", file);
        simpleJdbcInsert.execute(fileData);
        return id;
    }
}

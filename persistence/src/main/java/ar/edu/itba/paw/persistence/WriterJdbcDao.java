package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.WriterDao;
import ar.edu.itba.paw.models.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WriterJdbcDao implements WriterDao {

    private final static RowMapper<Writer> ROW_MAPPER = (rs, rowNum) -> new Writer(rs.getLong("writer_id"), rs.getString("name"), rs.getString("last_Name"), rs.getString("email"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public WriterJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS writers("+ "writer_id SERIAL PRIMARY KEY, " + "name VARCHAR(255) NOT NULL," + "last_name VARCHAR(255) NOT NULL," + "email VARCHAR(255) NOT NULL UNIQUE," + ")");
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .usingGeneratedKeyColumns("writer_ Id")
                .withTableName("writers");
    }

    @Override
    public Optional<Writer> findById(long id){
        final List<Writer> list = jdbcTemplate.query("SELECT * FROM writers WHERE writer_id = ?", new Object[] {id} ,ROW_MAPPER);
        return list.stream().findFirst();
    }

    @Override
    public Writer create(String name, String lastName, String email){
        Map<String, Object> writerData = new HashMap<>();
        writerData.put("email", email);
        writerData.put("name", name);
        writerData.put("last_name", lastName);
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(writerData);
        return new Writer(generatedId.longValue(), name, lastName, email);
    }
}

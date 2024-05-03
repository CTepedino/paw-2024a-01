package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.models.Image;
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
public class ImageJdbcDao implements ImageDao {

    private static final RowMapper<Image> ROW_MAPPER = (rs, rowNum) -> new Image(rs.getLong("image_id"), rs.getBytes("image"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public ImageJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .usingGeneratedKeyColumns("image_id")
                .withTableName("images");
    }

    @Override
    public Optional<Image> findById(long id) {
        final List<Image> list = jdbcTemplate.query(
            "SELECT * FROM images WHERE image_id = ?",
                new Object[] {id},
                ROW_MAPPER
        );

        return list.stream().findFirst();
    }

    @Override
    public Image create(byte[] image){
        Map<String, Object> imageData = new HashMap<>();
        imageData.put("image", image);
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(imageData);
        return new Image(generatedId.longValue(), image);
    }
}

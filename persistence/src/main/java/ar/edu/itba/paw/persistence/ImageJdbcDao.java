package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository

public class ImageJdbcDao implements ImageDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public ImageJdbcDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .usingGeneratedKeyColumns("image_id")
                .withTableName("images");
    }

    @Override
    public Optional<Image> findbyid(long imageId) {
        return jdbcTemplate.query("SELECT * FROM images WHERE image_id = ?",
                        new Object[]{imageId},
                        (rs, rowNum) -> new Image(rs.getInt("image_id"), rs.getBytes("photoblob")))
                .stream()
                .findFirst();
    }


    @Override
    public Image uploadImage(byte[] photoBlob) {
        Map<String, Object> imageData = new HashMap<>();
        imageData.put("photoblob", photoBlob);
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(imageData);
        return new Image(generatedId.intValue(), photoBlob);
    }

    @Override
    public void deleteImage(long image_id) {
        jdbcTemplate.update("DELETE FROM images WHERE image_id = ?", image_id);
    }

}

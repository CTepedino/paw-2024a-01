package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PdfDao;
import ar.edu.itba.paw.models.Pdf;
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
public class PdfJdbcDao implements PdfDao {

    private static final RowMapper<Pdf> ROW_MAPPER = (rs, rowNum) -> new Pdf(rs.getLong("pdf_id"), rs.getBytes("pdf"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public PdfJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .usingGeneratedKeyColumns("pdf_id")
                .withTableName("pdfs");
    }

    @Override
    public Optional<Pdf> findById(long id) {
        final List<Pdf> list = jdbcTemplate.query(
                "SELECT * FROM pdfs WHERE pdf_id = ?",
                new Object[] {id},
                ROW_MAPPER
        );

        return list.stream().findFirst();
    }

    @Override
    public Pdf create(byte[] pdf) {
        Map<String, Object> pdfData = new HashMap<>();
        pdfData.put("pdf", pdf);
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(pdfData);
        return new Pdf(generatedId.longValue(), pdf);
    }
}

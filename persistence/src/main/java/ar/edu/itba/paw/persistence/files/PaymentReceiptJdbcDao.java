package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.PaymentReceiptDao;
import ar.edu.itba.paw.models.files.PaymentReceipt;
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
public class PaymentReceiptJdbcDao implements PaymentReceiptDao {

    private static final RowMapper<PaymentReceipt> ROW_MAPPER = (rs, rowNum) -> new PaymentReceipt(rs.getLong("id"), rs.getBytes("file"), rs.getString("type"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public PaymentReceiptJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("payment_receipts");
    }


    @Override
    public void createOrUpdate(long id, byte[] file, String type) {
        if (findById(id).isPresent()) {
            update(id, file, type);
        } else {
            create(id, file, type);
        }
    }

    @Override
    public Optional<PaymentReceipt> findById(long id) {
        final List<PaymentReceipt> list = jdbcTemplate.query(
        """
                SELECT *
                FROM payment_receipts
                WHERE id = ?
            """,
            ROW_MAPPER,
            id
        );
        return list.stream().findFirst();
    }

    @Override
    public void update(long id, byte[] file, String type) {
        jdbcTemplate.update(
        """
               UPDATE payment_receipts
               SET file = ?,
               type = ?
               WHERE id = ?
            """,
            file,
            type,
            id
        );
    }

    @Override
    public long create(long id, byte[] file, String type) {
        Map<String, Object> fileData = new HashMap<>();
        fileData.put("id", id);
        fileData.put("file", file);
        fileData.put("type", type);
        simpleJdbcInsert.execute(fileData);
        return id;
    }
}

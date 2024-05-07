package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.PaymentReceiptDao;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PaymentReceiptJdbcDao extends FileJdbcDao<PaymentReceipt> implements PaymentReceiptDao {

    private static final RowMapper<PaymentReceipt> ROW_MAPPER = (rs, rowNum) -> new PaymentReceipt(rs.getLong("id"), rs.getBytes("file"));

    @Autowired
    public PaymentReceiptJdbcDao(DataSource ds) {
        super(ds, "payment_receipts", ROW_MAPPER);
    }

}

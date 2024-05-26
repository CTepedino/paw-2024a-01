package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.PaymentReceiptDao;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentReceiptJpaDao extends FileJpaDao<PaymentReceipt> implements PaymentReceiptDao {

    @Override
    public void createOrUpdate(long id, byte[] file, String type) {

    }

    @Override
    public void update(long id, byte[] file, String type) {

    }

    @Override
    public long create(long id, byte[] file, String type) {
        return 0;
    }
}

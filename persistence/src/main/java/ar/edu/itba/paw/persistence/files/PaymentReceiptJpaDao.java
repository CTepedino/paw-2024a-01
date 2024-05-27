package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.PaymentReceiptDao;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class PaymentReceiptJpaDao implements PaymentReceiptDao {

    @PersistenceContext
    private EntityManager em;

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
        return Optional.ofNullable(em.find(PaymentReceipt.class, id));
    }

    @Override
    public void update(long id, byte[] file, String type) {
        findById(id).ifPresent(receipt -> {
            receipt.setFile(file);
            receipt.setType(type);
            em.merge(receipt);
        });
    }

    @Override
    public long create(long id, byte[] file, String type) {
        PaymentReceipt paymentReceipt = new PaymentReceipt(id, file, type);
        em.persist(paymentReceipt);
        return id;
    }
}

package ar.edu.itba.paw.interfaces.dao.files;

import ar.edu.itba.paw.models.files.PaymentReceipt;

import java.util.Optional;

public interface PaymentReceiptDao{
    void createOrUpdate(long id, byte[] file, String type);

    Optional<PaymentReceipt> findById(long id);

    void update(long id, byte[] file, String type);

    long create(long id, byte[] file, String type);
}

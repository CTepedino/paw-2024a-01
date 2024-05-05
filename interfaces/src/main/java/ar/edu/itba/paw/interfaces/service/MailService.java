package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Order;

public interface MailService{

    void sendOrderEmail(long buyerId, long bookId);

    void sendRegisterEmail(long userId);

    void sendReceiptUploadedEmail(Order order);

    void sendReceiptApprovedEmail(Order order);

    void sendReceiptDeniedEmail(Order order);

}

package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.users.User;

import java.time.LocalDateTime;

public interface MailService{

    void sendRegisterEmail(User user, String code, LocalDateTime expiration);

    void sendReceiptUploadedEmail(Order order);

    void sendReceiptReuploadedEmail(Order order);

    void sendReceiptApprovedEmail(Order order);

    void sendReceiptDeniedEmail(Order order);

    void sendMissingDataEmail(User user);
}

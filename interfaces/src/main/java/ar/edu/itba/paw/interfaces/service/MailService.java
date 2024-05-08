package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.users.EmailValidation;
import ar.edu.itba.paw.models.users.User;

public interface MailService{

    void sendOrderEmail(User buyer, Book book);

    void sendRegisterEmail(User user, String code);

    void sendReceiptUploadedEmail(Order order);

    void sendReceiptApprovedEmail(Order order);

    void sendReceiptDeniedEmail(Order order);

}

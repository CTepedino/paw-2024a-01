package ar.edu.itba.paw.interfaces;

public interface MailService{

    void sendOrderEmail(long buyerId, long bookId);

    void sendRegisterEmail(long userId);

}

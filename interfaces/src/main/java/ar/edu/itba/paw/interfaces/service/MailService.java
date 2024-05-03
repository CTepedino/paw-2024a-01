package ar.edu.itba.paw.interfaces.service;

public interface MailService{

    public void sendOrderEmail(long buyerId, long bookId);
}

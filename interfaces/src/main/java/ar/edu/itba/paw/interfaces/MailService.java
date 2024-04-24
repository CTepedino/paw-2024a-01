package ar.edu.itba.paw.interfaces;

public interface MailService{

    public void sendOrderEmail(long buyerId, long bookId);
}

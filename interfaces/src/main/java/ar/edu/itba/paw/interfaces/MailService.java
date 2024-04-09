package ar.edu.itba.paw.interfaces;

public interface MailService{

    void sendEmail(String to, String name, String lastName, String readerEmail);
}

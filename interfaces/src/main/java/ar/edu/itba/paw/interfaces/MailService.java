package ar.edu.itba.paw.interfaces;

public interface MailService {

    String sendEmail(String to, String name, String lastName, String readerEmail);
}

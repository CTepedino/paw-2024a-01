package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MailService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailServiceImpl implements MailService {

    private final Session session;

    public MailServiceImpl(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("PAW", "^A^3ufK99dbu8L"); // Replace with your email credentials
            }
        });
    }

    public String sendEmail(String to, String name, String lastName, String readerEmail){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("contactinfosender@gmail.com")); // Replace with your email address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("A reader wants to buy your book");
            message.setText(String.format("The reader %s %s wants to buy your book. Please contact them at %s", name, lastName, readerEmail));

            // Send message
            Transport.send(message);

            return "Email sent succesfully!";
        } catch (MessagingException e){
            return e.getMessage();
        }
    }
}

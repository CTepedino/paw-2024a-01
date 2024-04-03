package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String sendEmail(String to, String name, String lastName, String readerEmail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("A reader wants to buy your book");
        message.setText(String.format("The reader %s %s is interested in buying your book. Please contact them at %s", name, lastName, readerEmail));

        try {
            javaMailSender.send(message);
            return "Email sent successfully!";
        } catch (MailException e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}

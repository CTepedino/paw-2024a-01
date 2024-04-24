package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    private final UserService us;
    private final BookService bs;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine, UserService us, BookService bs) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.us = us;
        this.bs = bs;
    }

    @Override
    @Async
    public void sendOrderEmail(long buyerId, long bookId){
        User buyer = us.findById(buyerId).orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        User writer = us.findById(book.getWriter().getId()).orElseThrow(UserNotFoundException::new);

        try {
            Context context = new Context();
            context.setVariable("readerEmail", buyer.getEmail());
            context.setVariable("bookTitle", book.getTitle());

            //TODO: contactEmailTemplate no deberia estar en la capa de webapp
            String emailContent = templateEngine.process("contactEmailTemplate", context);


            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setTo(writer.getEmail());
            messageHelper.setSubject("A reader wants to buy your book");
            messageHelper.setText(emailContent, true);


            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}

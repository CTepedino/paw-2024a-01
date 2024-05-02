package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



@Service
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final ResourceBundleMessageSource emailMessageSource;
    private final Environment env;

    private final UserService us;
    private final BookService bs;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine thymeleafTemplateEngine, ResourceBundleMessageSource emailMessageSource, Environment env, UserService us, BookService bs) {
        this.javaMailSender = javaMailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.emailMessageSource = emailMessageSource;
        this.env = env;
        this.us = us;
        this.bs = bs;
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }

    private void sendMessageUsingTemplate(String to, String subject, String template, Map<String, Object> templateModel, Locale locale) throws MessagingException {

        Context thymeleafContext = new Context(locale);
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    @Override
    @Async
    public void sendOrderEmail(long buyerId, long bookId){
        User buyer = us.findById(buyerId).orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        User writer = us.findById(book.getWriter().getId()).orElseThrow(UserNotFoundException::new);

        Locale currentLocale = LocaleContextHolder.getLocale();// TODO: Que locale usar para los mails?
        String to = writer.getEmail();
        String subject = emailMessageSource.getMessage("mail.orderEmail.subject", null, currentLocale);
        HashMap<String, Object> data = new HashMap<>();
        data.put("buyerFirstName", buyer.getFirstName());
        data.put("buyerLastName", buyer.getLastName());
        data.put("bookTitle", book.getTitle());
        data.put("url", env.getProperty("baseUrl"));

        try {
            LOGGER.atDebug().setMessage("Sending order email to: {}").addArgument(to).log();
            sendMessageUsingTemplate(to, subject, "orderEmailTemplate", data, currentLocale);
        } catch (MessagingException e){
            LOGGER.atDebug().setMessage("Failed to send order email to: {} \n Error Message: {}").addArgument(to).addArgument(e.getMessage()).log();
        }
        LOGGER.atDebug().setMessage("Sent order email to: {}").addArgument(to).log();
    }

    @Override
    @Async
    public void sendRegisterEmail(long userId){
        User user = us.findById(userId).orElseThrow(UserNotFoundException::new);

        Locale currentLocale = LocaleContextHolder.getLocale();
        String to = user.getEmail();
        String subject = emailMessageSource.getMessage("mail.registerEmail.subject", null, currentLocale);
        HashMap<String, Object> data = new HashMap<>();
        data.put("userFirstName", user.getFirstName());
        data.put("userLastName", user.getLastName());
        data.put("url", env.getProperty("baseUrl"));
        data.put("profileUrl", env.getProperty("baseUrl") + "/profile");

        try {
            LOGGER.atDebug().setMessage("Sending register email to: {}").addArgument(to).log();
            sendMessageUsingTemplate(to, subject, "registerEmailTemplate", data, currentLocale);
        } catch (MessagingException e){
            LOGGER.atDebug().setMessage("Failed to send register email to: {} \n Error Message: {}").addArgument(to).addArgument(e.getMessage()).log();
        }
        LOGGER.atDebug().setMessage("Sent register email to: {}").addArgument(to).log();

    }

}

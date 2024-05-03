package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final ResourceBundleMessageSource emailMessageSource;

    private final UserService us;
    private final BookService bs;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine thymeleafTemplateEngine, ResourceBundleMessageSource emailMessageSource, UserService us, BookService bs) {
        this.javaMailSender = javaMailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.emailMessageSource = emailMessageSource;
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

    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("template-thymeleaf.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    @Override
    @Async
    public void sendOrderEmail(long buyerId, long bookId){
        User buyer = us.findById(buyerId).orElseThrow(UserNotFoundException::new);
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        User writer = us.findById(book.getWriter().getId()).orElseThrow(UserNotFoundException::new);

        Locale currentLocale = LocaleContextHolder.getLocale();// TODO: Que locale usar para los mails?

        Context context = new Context(currentLocale);
        context.setVariable("buyerFirstName", buyer.getFirstName());
        context.setVariable("buyerLastName", buyer.getLastName());
        context.setVariable("bookTitle", book.getTitle());

        String emailContent = thymeleafTemplateEngine.process("orderEmailTemplate", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setTo(writer.getEmail());
            messageHelper.setSubject(emailMessageSource.getMessage("mail.orderEmail.subject", null, currentLocale));
            messageHelper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e){
            LOGGER.atDebug().setMessage("Failed to send email to: {} \n Error Message: {}").addArgument(writer.getEmail()).addArgument(e.getMessage()).log();
        }
        LOGGER.atDebug().setMessage("Sent order Email to: {}").addArgument(writer.getEmail()).log();
    }

}

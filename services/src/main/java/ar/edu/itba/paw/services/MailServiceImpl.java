package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



@Service
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final ResourceBundleMessageSource emailMessageSource;
    private final Environment env;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine thymeleafTemplateEngine, ResourceBundleMessageSource emailMessageSource, Environment env) {
        this.javaMailSender = javaMailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.emailMessageSource = emailMessageSource;
        this.env = env;
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
    public void sendRegisterEmail(User user, String code, LocalDateTime expiration){

        Locale currentLocale = user.getLocale();
        String to = user.getEmail();
        String subject = emailMessageSource.getMessage("mail.registerEmail.subject", null, currentLocale);
        HashMap<String, Object> data = new HashMap<>();
        data.put("userFirstName", user.getFirstName());
        data.put("userLastName", user.getLastName());
        data.put("url", env.getProperty("baseUrl"));
        data.put("validateUrl", env.getProperty("baseUrl") + "/validate?id=" + user.getUserId() + "&code=" + code);
        data.put("expiration", expiration.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(currentLocale)));

        try {
            LOGGER.atDebug().setMessage("Sending register email to: {}").addArgument(to).log();
            sendMessageUsingTemplate(to, subject, "registerEmailTemplate", data, currentLocale);
        } catch (MessagingException e){
            LOGGER.atWarn().setMessage("Failed to send register email to: {} - Error Message: {}").addArgument(to).addArgument(e.getMessage()).log();
        }
        LOGGER.atDebug().setMessage("Sent register email to: {}").addArgument(to).log();

    }

    @Override
    @Async
    public void sendReceiptUploadedEmail(Order order){
        User writer = order.getWriter();
        User buyer = order.getBuyer();
        Book book = order.getBook();

        Locale currentLocale = writer.getLocale();
        String to = writer.getEmail();
        String subject = emailMessageSource.getMessage("mail.receiptUploadedEmail.subject", null, currentLocale);
        HashMap<String, Object> data = new HashMap<>();
        data.put("buyerFirstName", buyer.getFirstName());
        data.put("buyerLastName", buyer.getLastName());
        data.put("bookTitle", book.getTitle());
        data.put("url", env.getProperty("baseUrl"));
        data.put("salesUrl", env.getProperty("baseUrl") + "/sales");

        try {
            LOGGER.atDebug().setMessage("Sending Receipt Uploaded email to: {}").addArgument(to).log();
            sendMessageUsingTemplate(to, subject, "receiptUploadedEmailTemplate", data, currentLocale);
        } catch (MessagingException e){
            LOGGER.atWarn().setMessage("Failed to send Receipt Uploaded email to: {} - Error Message: {}").addArgument(to).addArgument(e.getMessage()).log();
        }
        LOGGER.atDebug().setMessage("Sent Receipt Uploaded email to: {}").addArgument(to).log();
    }

    @Override
    @Async
    public void sendReceiptReuploadedEmail(Order order) {
        User writer = order.getWriter();
        User buyer = order.getBuyer();
        Book book = order.getBook();

        Locale currentLocale = writer.getLocale();
        String to = writer.getEmail();
        String subject = emailMessageSource.getMessage("mail.receiptReuploadedEmail.subject", null, currentLocale);
        HashMap<String, Object> data = new HashMap<>();
        data.put("buyerFirstName", buyer.getFirstName());
        data.put("buyerLastName", buyer.getLastName());
        data.put("bookTitle", book.getTitle());
        data.put("url", env.getProperty("baseUrl"));
        data.put("salesUrl", env.getProperty("baseUrl") + "/sales");

        try {
            LOGGER.atDebug().setMessage("Sending Receipt Reuploaded email to: {}").addArgument(to).log();
            sendMessageUsingTemplate(to, subject, "receiptReuploadedEmailTemplate", data, currentLocale);
        } catch (MessagingException e){
            LOGGER.atWarn().setMessage("Failed to send Receipt Reuploaded email to: {} - Error Message: {}").addArgument(to).addArgument(e.getMessage()).log();
        }
        LOGGER.atDebug().setMessage("Sent Receipt Reuploaded email to: {}").addArgument(to).log();
    }

    @Override
    @Async
    public void sendReceiptApprovedEmail(Order order){
        User buyer = order.getBuyer();
        Book book = order.getBook();

        Locale currentLocale = buyer.getLocale();
        String to = buyer.getEmail();
        String subject = emailMessageSource.getMessage("mail.receiptApprovedEmail.subject", null, currentLocale);
        HashMap<String, Object> data = new HashMap<>();
        data.put("bookTitle", book.getTitle());
        data.put("url", env.getProperty("baseUrl"));
        data.put("bookUrl", env.getProperty("baseUrl") + "/book/" + book.getBookId());

        try {
            LOGGER.atDebug().setMessage("Sending Receipt Approved email to: {}").addArgument(to).log();
            sendMessageUsingTemplate(to, subject, "receiptApprovedEmailTemplate", data, currentLocale);
        } catch (MessagingException e){
            LOGGER.atWarn().setMessage("Failed to send Receipt Approved email to: {} - Error Message: {}").addArgument(to).addArgument(e.getMessage()).log();
        }
        LOGGER.atDebug().setMessage("Sent Receipt Approved email to: {}").addArgument(to).log();
    }

    @Override
    @Async
    public void sendReceiptDeniedEmail(Order order){
        User buyer = order.getBuyer();
        Book book = order.getBook();

        Locale currentLocale = buyer.getLocale();
        String to = buyer.getEmail();
        String subject = emailMessageSource.getMessage("mail.receiptDeniedEmail.subject", null, currentLocale);
        HashMap<String, Object> data = new HashMap<>();
        data.put("bookTitle", book.getTitle());
        data.put("url", env.getProperty("baseUrl"));
        data.put("purchasesUrl", env.getProperty("baseUrl") + "/purchases");

        try {
            LOGGER.atDebug().setMessage("Sending Receipt Denied email to: {}").addArgument(to).log();
            sendMessageUsingTemplate(to, subject, "receiptDeniedEmailTemplate", data, currentLocale);
        } catch (MessagingException e){
            LOGGER.atWarn().setMessage("Failed to send Receipt Denied email to: {} - Error Message: {}").addArgument(to).addArgument(e.getMessage()).log();
        }
        LOGGER.atDebug().setMessage("Sent Receipt Denied email to: {}").addArgument(to).log();
    }

    @Override
    @Async
    public void sendMissingDataEmail(User user){
        Locale currentLocale = user.getLocale();
        String to = user.getEmail();
        String subject = emailMessageSource.getMessage("mail.missingInfo.subject", null, currentLocale);
        HashMap<String, Object> data = new HashMap<>();
        data.put("url", env.getProperty("baseUrl"));
        data.put("purchasesUrl", env.getProperty("baseUrl") + "/profile");

        try {
            LOGGER.atDebug().setMessage("Sending Missing info email to: {}").addArgument(to).log();
            sendMessageUsingTemplate(to, subject, "missingInfoEmailTemplate", data, currentLocale);
        } catch (MessagingException e){
            LOGGER.atWarn().setMessage("Failed to send Missing info email to: {} - Error Message: {}").addArgument(to).addArgument(e.getMessage()).log();
        }
        LOGGER.atDebug().setMessage("Sent Missing info email to: {}").addArgument(to).log();
    }


}

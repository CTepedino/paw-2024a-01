package ar.edu.itba.paw.webapp.config;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeUnit;

@EnableWebMvc
@EnableTransactionManagement
@EnableAsync
@ComponentScan({ "ar.edu.itba.paw.webapp.controller",
                "ar.edu.itba.paw.webapp.form.validations",
                "ar.edu.itba.paw.services",
                "ar.edu.itba.paw.persistence"})
@Configuration
@PropertySource("classpath:application.properties")
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final long MAX_UPLOAD_SIZE = 10L * 1024 * 1024;

    @Autowired
    private Environment env;

    @Value("classpath:schema.sql")
    private Resource schemaSql;

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver viewResolver = new
                InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public MessageSource messageSource(){
        final ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.addBasenames("classpath:i18n/messages");
        ms.setDefaultEncoding(StandardCharsets.UTF_8.name());
        ms.setCacheSeconds((int) TimeUnit.MINUTES.toSeconds(5));
        return ms;
    }

    /* Descomentar para ver la pagina en español sin tener que cambiar los settings del browser
    @Bean
    public LocaleResolver localeResolver(){
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("es"));
        return slr;
    }*/

    @Bean
    public DataSource dataSource(){
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(org.postgresql.Driver.class);
        ds.setUrl(env.getProperty("jdbc.url"));
        ds.setUsername(env.getProperty("jdbc.username"));
        ds.setPassword(env.getProperty("jdbc.password"));
        return ds;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource ds){
        final DataSourceInitializer dsi = new DataSourceInitializer();

        dsi.setDataSource(ds);
        dsi.setDatabasePopulator(databasePopulator());

        return dsi;
    }

    private DatabasePopulator databasePopulator(){
        final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();

        dbp.addScript(schemaSql);

        return dbp;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setPort(25);
        mailSender.setUsername(env.getProperty("mail.username"));
        mailSender.setPassword(env.getProperty("mail.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.mime.charset", "UTF-8");

        return mailSender;
    }

    @Bean
    public ITemplateResolver thymeleafTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("mails/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());

        return templateEngine;
    }

    @Bean
    public ResourceBundleMessageSource emailMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mailMessages");
        return messageSource;
    }

    @Bean
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(MAX_UPLOAD_SIZE);
        return multipartResolver;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final DataSource ds){
        return new DataSourceTransactionManager(ds);
    }

}

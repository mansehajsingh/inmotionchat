package com.inmotionchat.notifications.config;

import com.inmotionchat.notifications.NotificationsService;
import com.inmotionchat.notifications.model.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Configuration
public class EmailConfiguration {

    private final NotificationsService notificationsService;

    @Autowired
    public EmailConfiguration(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @Bean
    public TemplateEngine springTemplateEngine() {
        ClassLoaderTemplateResolver emailTemplateResolver =  new ClassLoaderTemplateResolver();
        emailTemplateResolver.setPrefix("/templates/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
        emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        emailTemplateResolver.setCacheable(true);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(emailTemplateResolver);

        return templateEngine;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        EmailProperties emailProps = notificationsService.getEmailProperties();

        javaMailSender.setHost(emailProps.getHost());
        javaMailSender.setUsername(emailProps.getUsername());
        javaMailSender.setPassword(emailProps.getPassword());
        javaMailSender.setPort(emailProps.getPort());
        javaMailSender.setProtocol("smtp");

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        javaMailSender.setJavaMailProperties(properties);

        return javaMailSender;
    }

}

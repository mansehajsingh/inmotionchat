package com.inmotionchat.notifications.notifiers;

import com.inmotionchat.core.data.events.UnverifiedUserEvent;
import com.inmotionchat.notifications.NotificationsService;
import com.inmotionchat.notifications.model.Email;
import com.inmotionchat.notifications.model.NotifierType;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailNotifier implements Notifier {

    private final Logger log = LoggerFactory.getLogger(EmailNotifier.class);

    private final TaskExecutor executor;

    private final TemplateEngine templateEngine;

    private final JavaMailSender mailSender;

    private final NotificationsService notificationsService;

    @Autowired
    public EmailNotifier(NotificationsService notificationsService,
                         TaskExecutor executor,
                         TemplateEngine templateEngine,
                         JavaMailSender mailSender) {
        this.notificationsService = notificationsService;
        this.executor = executor;
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    @Override
    public NotifierType getNotifierType() {
        return NotifierType.EMAIL;
    }


    @Override
    public void sendNotification(Object details) {

        Map<String, Object> properties = new HashMap<>();

        if (details instanceof UnverifiedUserEvent userVerify) {

            properties.put("uid", userVerify.uid());
            properties.put("displayName", userVerify.displayName());

            String content =
                    this.templateEngine.process("UserVerify", new Context(Locale.getDefault(), properties));

            Email email = new Email(
                    userVerify.email(),
                    "Verify your email to get started with In Motion.",
                    "UserVerify",
                    content
            );

            SendEmailTask task = new SendEmailTask(email);

            this.executor.execute(task);

        } else {

            log.warn("Email notifier was given event details of type {} but did not know how to handle it.",
                    details.getClass().getName());

        }

    }

    private class SendEmailTask implements Runnable {

        private final Email email;

        public SendEmailTask(Email email) {
            this.email = email;
        }

        @Override
        public void run() {
            try {
                MimeMessage mail = mailSender.createMimeMessage();

                mail.setFrom(notificationsService.getEmailProperties().getUsername());
                mail.setRecipient(Message.RecipientType.TO, new InternetAddress(email.recipient()));
                mail.setSubject(email.subject());
                mail.setContent(email.htmlContent(), "text/html; charset=utf-8");

                mailSender.send(mail);

            } catch (Exception e) {
                log.error("Something went wrong when sending an email: {}", e.getMessage());
            }
        }

    }

}

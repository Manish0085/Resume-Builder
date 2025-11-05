package com.build.resume.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${app.email.from}")
    private String fromEmail;

    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender sender){
        this.mailSender = sender;
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {

        log.info("Inside EmailService - sendHtmlEmail(): {}, {}, {}", to, subject, htmlContent);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }


    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String fileName) throws MessagingException {

        log.info("Inside EmailService - sendEmailWithAttachment()");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);
        helper.addAttachment(fileName, new ByteArrayResource(attachment));
        mailSender.send(message);
    }



}

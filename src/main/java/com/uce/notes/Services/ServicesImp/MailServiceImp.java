package com.uce.notes.Services.ServicesImp;

import com.uce.notes.Model.Dto.Mail;
import com.uce.notes.Services.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImp implements MailService {

    @Value("${email.sender}")
    private String emailUser;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            messageHelper.setFrom(emailUser);
            messageHelper.setTo(mail.getToUser());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mail.getMessage(), true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);
    }
}

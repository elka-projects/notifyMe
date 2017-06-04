package com.notifyme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Created by raqu on 03.06.17.
 */

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(
            String to,
            String subject,
            String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendTemplatedMessage (
            String to,
            Template template) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        String title = String.format("[%s] %s, from: %s",
                template.getProject(),
                template.getTitle(),
                template.getAuthor());

        message.setSubject(title);
        message.setText(template.getContent());
        emailSender.send(message);
    }


}

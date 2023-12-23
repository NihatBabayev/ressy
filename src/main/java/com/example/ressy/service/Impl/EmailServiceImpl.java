package com.example.ressy.service.Impl;

import com.example.ressy.dto.EmailRequest;
import com.example.ressy.service.EmailService;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void publishMessage(String title, String text, String recipient) {
//        Map<String, String> notification = new HashMap<>();
//        notification.put("title", title);
//        notification.put("text", text);
//        notification.put("recipient", recipient);
//        String messageJson = "{\"title\":\"" + title + "\",\"text\":\"" + text + "\",\"recipient\":\"" + recipient + "\"}";
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTitle(title);
        emailRequest.setText(text);
        emailRequest.setRecipient(recipient);


//        rabbitTemplate.convertAndSend("notification_queue", notification);
//         rabbitTemplate.convertAndSend("notification_queue", notification, message -> {
//             MessageProperties properties = message.getMessageProperties();
//             properties.setContentEncoding("UTF-8");
//             return message;
//         });
        rabbitTemplate.convertAndSend("notification_queue", emailRequest, message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setContentEncoding("UTF-8");
            return message;
        });

    }


    @Async
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailsAsync(List<String> emails, String subject, String message) {
        for (String email : emails) {
            sendEmail(email, subject, message);
        }
    }
}

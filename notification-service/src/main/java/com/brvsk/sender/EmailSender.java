package com.brvsk.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j

class EmailSender implements MessageSender {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    public EmailSender(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMessage(final String to, final String title, final String body) throws Exception {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(body);
        javaMailSender.send(simpleMailMessage);

    }
}

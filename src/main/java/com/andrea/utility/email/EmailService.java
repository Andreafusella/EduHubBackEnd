package com.andrea.utility.email;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class EmailService {
    private String from;
    private String host;
    private String password;

    public EmailService(String from, String password) {
        this.from = from;
        this.host = "smtp.gmail.com";
        this.password = password;
    }

    public void sendEmail(String to, String subject, String body) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);
//            message.setContent(); per mandare anche html

            Transport.send(message);
            System.out
                    .println("Email sent successfully to " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}

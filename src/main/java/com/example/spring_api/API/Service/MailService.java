package com.example.spring_api.API.Service;

import java.util.Properties;
import org.springframework.stereotype.Service;
import jakarta.mail.*;
import jakarta.mail.internet.*;

@Service
public class MailService {

    private final String from;
    private final String password;
    private final String host;

    // Default constructor
    public MailService() {
        this("thkinh2008@gmail.com", "yuemkfwazqipomrg", "smtp.gmail.com");
    }

    // Parameterized constructor
    public MailService(String from, String password, String host) {
        this.from = from;
        this.password = password;
        this.host = host;
    }

    public Boolean sendCode(String to, String code) {
        String subject = "Android Pot Hole Detector";
        String body = "Your secret code: "+ code;
        return sendEmail(to, subject, body);
    }

    public Boolean signUpNotification(String to) {
        String subject = "Android Pot Hole Detector";
        String body = "Hello " + to + "\n\nWelcome to our app for the first time !!!";
        return sendEmail(to, subject, body);
    }

    private Boolean sendEmail(String to, String subject, String body) {
        try {
            // Set up properties for the SMTP server
            Properties properties = setupMailProperties();

            // Create a new session with an authenticator
            Session session = createSession(properties);

            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

    private Properties setupMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        return properties;
    }

    private Session createSession(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
    }
}

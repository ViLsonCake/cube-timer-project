package com.project.SpringCubeTimer.sendMail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailSender {

    @Value("SenderEmail")
    private String sender;

    @Value("SenderPassword")
    private String password;

    private String recipient, subject, text, host, smtpPort;

    public MailSender(String recipient, String subject, String text) {
        this.recipient = recipient;
        this.subject = subject;
        this.text = text;

        // smtp config
        this.host = "smtp.gmail.com";
        this.smtpPort = "465";
    }

    public void send() {
        // Create properties
        Properties properties = new Properties();
        {
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", smtpPort);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
        }

        // Create session
        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, password);
                    }
                }
        );

        // Enable debug mode
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);

            // Set message properties
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(text);

            // Send message
            Transport.send(message);

        } catch (SendFailedException e) {
            e.getInvalidAddresses();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}

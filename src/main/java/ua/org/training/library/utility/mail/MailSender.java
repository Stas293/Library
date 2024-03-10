package ua.org.training.library.utility.mail;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.InjectProperty;

import java.util.Properties;

@Component
@Slf4j
public class MailSender {
    @InjectProperty("mail.smtp.host")
    private String mailSmtpHost;

    @InjectProperty("mail.smtp.port")
    private String mailSmtpPort;

    @InjectProperty("mail.smtp.auth")
    private String mailSmtpAuth;

    @InjectProperty("mail.smtp.starttls.enable")
    private String mailSmtpStarttlsEnable;

    @InjectProperty("mail.smtp.ssl.trust")
    private String mailSmtpSslTrust;

    @InjectProperty("mail.username")
    private String mailUsername;

    @InjectProperty("mail.password")
    private String mailPassword;

    public void sendMail(String to, String subject, String text) {
        Properties properties = setProperties();
        Session session = createSession(properties);
        createAndSendMail(to, subject, text, session);
    }

    private Session createSession(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUsername, mailPassword);
            }
        });
    }

    @SneakyThrows
    private void createAndSendMail(String to, String subject, String text, Session session) {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailUsername));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
    }

    private Properties setProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", mailSmtpHost);
        properties.put("mail.smtp.port", mailSmtpPort);
        properties.put("mail.smtp.auth", mailSmtpAuth);
        properties.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        properties.put("mail.smtp.ssl.trust", mailSmtpSslTrust);
        return properties;
    }
}

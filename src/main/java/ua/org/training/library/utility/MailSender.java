package ua.org.training.library.utility;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class MailSender {
    private static final Logger LOGGER = LogManager.getLogger(MailSender.class);

    private static final String MAIL_SMTP_HOST = Utility.getApplicationProperty("mail.smtp.host");
    private static final String MAIL_SMTP_PORT = Utility.getApplicationProperty("mail.smtp.port");
    private static final String MAIL_SMTP_AUTH = Utility.getApplicationProperty("mail.smtp.auth");
    private static final String MAIL_SMTP_STARTTLS_ENABLE = Utility.getApplicationProperty("mail.smtp.starttls.enable");
    private static final String MAIL_SMTP_SSL_TRUST = Utility.getApplicationProperty("mail.smtp.ssl.trust");
    private static final String MAIL_USERNAME = Utility.getApplicationProperty("mail.username");
    private static final String MAIL_PASSWORD = Utility.getApplicationProperty("mail.password");

    public void sendMail(String to, String subject, String text) {
        Properties properties = setProperties();
        Session session = createSession(properties);
        try {
            createAndSendMail(to, subject, text, session);
        } catch (MessagingException e) {
            LOGGER.error("Error sending email", e);
        }
    }

    private Session createSession(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAIL_USERNAME, MAIL_PASSWORD);
            }
        });
    }

    private void createAndSendMail(String to, String subject, String text, Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(MAIL_USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
    }

    private Properties setProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", MAIL_SMTP_HOST);
        properties.put("mail.smtp.port", MAIL_SMTP_PORT);
        properties.put("mail.smtp.auth", MAIL_SMTP_AUTH);
        properties.put("mail.smtp.starttls.enable", MAIL_SMTP_STARTTLS_ENABLE);
        properties.put("mail.smtp.ssl.trust", MAIL_SMTP_SSL_TRUST);
        return properties;
    }
}

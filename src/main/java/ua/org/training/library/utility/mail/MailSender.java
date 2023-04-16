package ua.org.training.library.utility.mail;

import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.context.annotations.InjectProperty;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Component
@Slf4j
public class MailSender {
    @InjectProperty("mail.smtp.host")
    private String MAIL_SMTP_HOST;
    @InjectProperty("mail.smtp.port")
    private String MAIL_SMTP_PORT;
    @InjectProperty("mail.smtp.auth")
    private String MAIL_SMTP_AUTH;
    @InjectProperty("mail.smtp.starttls.enable")
    private String MAIL_SMTP_STARTTLS_ENABLE;
    @InjectProperty("mail.smtp.ssl.trust")
    private String MAIL_SMTP_SSL_TRUST;
    @InjectProperty("mail.username")
    private String MAIL_USERNAME;
    @InjectProperty("mail.password")
    private String MAIL_PASSWORD;

    public void sendMail(String to, String subject, String text) {
        Properties properties = setProperties();
        Session session = createSession(properties);
        createAndSendMail(to, subject, text, session);
    }

    private Session createSession(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAIL_USERNAME, MAIL_PASSWORD);
            }
        });
    }

    @SneakyThrows
    private void createAndSendMail(String to, String subject, String text, Session session) {
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

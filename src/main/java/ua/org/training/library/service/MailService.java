package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Order;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.MailSender;
import ua.org.training.library.utility.Utility;

import java.text.MessageFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class MailService {
    private static final Logger LOGGER = LogManager.getLogger(MailService.class);
    private final MailSender mailSender;

    public MailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderMail(Locale locale, Order order) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                Constants.BUNDLE_NAME,
                locale);
        String bookName = order
                .getBook()
                .getName();
        String authors = order
                .getBook()
                .getAuthors()
                .stream()
                .map(Author::getFullName)
                .toList()
                .toString();
        String dateExpire = order.getDateExpire() != null ?
                order.getDateExpire()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString() :
                bundle.getString(Constants.BUNDLE_ORDER_STATUS_PREFIX + "not_set");
        String place = bundle.
                getString(Constants.BUNDLE_PLACE_PREFIX +
                        order.getPlace()
                                .getName()
                                .toLowerCase()
                                .replace(" ", ".")
                );
        String status = bundle
                .getString(Constants.BUNDLE_ORDER_STATUS_PREFIX +
                        order.getStatus()
                                .getCode()
                                .toLowerCase()
                );
        String subject = bundle
                .getString(Constants.Mail.MAIL_SUBJECT_CHANGE_ORDER_STATUS);
        String message =
                MessageFormat.format("{0}\n{1}: {2}\n{3}: {4}\n{5}: {6}\n{7}: {8}\n{9}: {10}",
                        bundle.getString(Constants.Mail.MAIL_TEXT_CHANGE_ORDER_STATUS),
                        bundle.getString(Constants.Mail.MAIL_BOOK_NAME), bookName,
                        bundle.getString(Constants.Mail.MAIL_BOOK_AUTHORS), authors,
                        bundle.getString(Constants.Mail.MAIL_ORDER_EXPIRATION), dateExpire,
                        bundle.getString(Constants.Mail.MAIL_ORDER_PLACE), place,
                        bundle.getString(Constants.Mail.MAIL_STATUS), status);
        new Thread(() -> mailSender.sendMail(
                order.getUser().getEmail(),
                subject,
                message)
        ).start();
    }

    public String sendResetPasswordMail(Locale locale, String email) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                Constants.BUNDLE_NAME,
                locale);
        String code = Utility.generateCode();
        String subject = bundle.getString(Constants.Mail.MAIL_SUBJECT_RESET_PASSWORD);
        String message = bundle.getString(Constants.Mail.MAIL_TEXT_RESET_PASSWORD) + "\n" +
                "http://localhost:8080/library/reset-password?code=" + code +
                "&email=" + email;
        LOGGER.info(String.format("Sending link to reset password %s", email));
        new Thread(() -> mailSender.sendMail(
                email,
                subject,
                message)
        ).start();
        return code;
    }
}

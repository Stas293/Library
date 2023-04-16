package ua.org.training.library.utility.mail;

import com.project.university.system_library.constants.Values;
import com.project.university.system_library.context.annotations.Autowired;
import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.enums.Mail;
import com.project.university.system_library.model.Author;
import com.project.university.system_library.model.Order;
import com.project.university.system_library.utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class MailService {
    private final MailSender mailSender;
    private final ExecutorService executorService;

    @Autowired
    public MailService(MailSender mailSender) {
        this.mailSender = mailSender;
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }

    public void sendOrderMail(Locale locale, Order order) {
        String bookName = order
                .getBook()
                .getTitle();
        String authors = order
                .getBook()
                .getAuthors()
                .stream()
                .map(Author::getFullName)
                .toList()
                .toString();
        String dateExpire = order.getDateExpire() != null ?
                order.getDateExpire()
                        .toString() :
                Utility.getBundleInterface(locale, Values.BUNDLE_ORDER_STATUS_NOT_SET);
        String status = Utility.getBundleInterface(locale,
                Values.BUNDLE_ORDER_STATUS_PREFIX +
                        order.getStatus()
                                .getCode()
                                .toLowerCase()
                );
        sendMail(order, locale, bookName, authors, dateExpire, status);
    }

    private void sendMail(Order order, Locale locale, String bookName,
                          String authors, String dateExpire, String status) {
        String subject = Utility.getBundleInterface(locale, Mail.MAIL_SUBJECT_CHANGE_ORDER_STATUS.getValue());
        String message = getMessage(locale, bookName, authors, dateExpire, status);
        Thread.startVirtualThread(() -> mailSender.sendMail(
                order.getUser().getEmail(),
                subject,
                message)
        );
    }

    @NotNull
    private static String getMessage(Locale locale, String bookName,
                                     String authors, String dateExpire, String status) {
        return MessageFormat.format("{0}\n{1}: {2}\n{3}: {4}\n{5}: {6}\n{7}: {8}",
                Utility.getBundleInterface(locale, Mail.MAIL_TEXT_CHANGE_ORDER_STATUS.getValue()),
                Utility.getBundleInterface(locale, Mail.MAIL_BOOK_NAME.getValue()), bookName,
                Utility.getBundleInterface(locale, Mail.MAIL_BOOK_AUTHORS.getValue()), authors,
                Utility.getBundleInterface(locale, Mail.MAIL_ORDER_EXPIRATION.getValue()), dateExpire,
                Utility.getBundleInterface(locale, Mail.MAIL_STATUS.getValue()), status);
    }

    public String sendResetPasswordMail(Locale locale, String email) {
        String code = UUID.randomUUID().toString();
        String subject = Utility.getBundleInterface(locale, Mail.MAIL_SUBJECT_RESET_PASSWORD.getValue());
        String message = Utility.getBundleInterface(locale, Mail.MAIL_TEXT_RESET_PASSWORD.getValue()) + "\n" +
                "http://localhost:8080/library/reset-password?code=" + code +
                "&email=" + email;
        log.info(String.format("Sending link to reset password %s", email));
        executorService.execute(() -> mailSender.sendMail(
                email,
                subject,
                message)
        );
        return code;
    }
}

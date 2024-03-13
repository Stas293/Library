package ua.org.training.library.enums;

public enum Mail {
    MAIL_SUBJECT_CHANGE_ORDER_STATUS("mail.subject.change.order.status"),
    MAIL_TEXT_CHANGE_ORDER_STATUS("mail.text.change.order.status"),
    MAIL_BOOK_NAME("mail.book.name"),
    MAIL_BOOK_AUTHORS("mail.book.authors"),
    MAIL_ORDER_EXPIRATION("mail.order.expiration"),
    MAIL_STATUS("mail.status"),
    MAIL_SUBJECT_RESET_PASSWORD("mail.subject.reset.password"),
    MAIL_TEXT_RESET_PASSWORD("mail.text.reset.password"),
    ;


    private final String value;

    Mail(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

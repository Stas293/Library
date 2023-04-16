package ua.org.training.library.constants;

import java.time.LocalDate;

public interface Values {
    String APP_DEFAULT_LOCALE = "en";
    String BUNDLE_NAME = "interface";
    String BUNDLE_CURRENCY_EXCHANGE = "currency_exchange";
    int DEFAULT_CURRENCY_EXCHANGE = 40;
    String BUNDLE_CURRENCY = "currency";
    String BUNDLE_LANGUAGE_FOR_BOOK = "language_for_book";
    LocalDate MONTH = LocalDate.now().plusMonths(1);
    String APP_UNAUTHORIZED_USER = "anonymous";
    String USER_ATTRIBUTE = "user";
    String LOGGED_USERS_SET_CONTEXT = "loggedUsers";
    String APP_ENCODING = "UTF-8";
    String[] APP_ROLES = new String[]{
            "ADMIN",
            "LIBRARIAN",
            "USER"};
    String APP_LANG_ATTRIBUTE = "lang";
    String LIBRARY_PATH = "/library";
    String APP_MESSAGE_ATTRIBUTE = "message";
    Object BUNDLE_ACCESS_DENIED_MESSAGE = "access.denied.message";
    int APP_BCRYPT_SALT = 10;
    String APP_STRING_DEFAULT_VALUE = "";
    String APP_LOCALE = "lang";
    String ROLE_USER = "USER";
    String BUNDLE_ORDER_STATUS_NOT_SET = "order.status.not.set";
    String BUNDLE_ORDER_STATUS_PREFIX = "order.status.";
    String CODE = "code";
    String EMAIL = "email";
    String APP_DEFAULT_LANGUAGE = "en";
    String REGISTERED = "REGISTER";
    long DEFAULT_ID = -1;
    String POST_METHOD = "POST";
    String BUNDLE_ACCESS_DENIED_LOGGED_USERS = "access.denied.logged.user";
}

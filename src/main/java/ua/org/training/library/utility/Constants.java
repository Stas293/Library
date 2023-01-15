package ua.org.training.library.utility;

import java.util.Date;

public interface Constants {

    interface RequestAttributes {
        String ACCOUNT_CONFIRM_PASSWORD_ATTRIBUTE = "confirmPassword";
        String ACCOUNT_FIRST_NAME_ATTRIBUTE = "firstName";
        String ACCOUNT_EMAIL_ATTRIBUTE = "email";
        String ACCOUNT_ID_ATTRIBUTE = "id";
        String ACCOUNT_LAST_NAME_ATTRIBUTE = "lastName";
        String ACCOUNT_PHONE_ATTRIBUTE = "phone";
        String APP_ERROR_ATTRIBUTE = "error";
        String LOGGED_USERS_SET_CONTEXT = "loggedUsers";
        String APP_LANG_ATTRIBUTE = "lang";
        String APP_MESSAGE_ATTRIBUTE = "message";
        String APP_PASSWORD_ATTRIBUTE = "password";
        String USER_ATTRIBUTE = "user";
        String APP_LOGIN_ATTRIBUTE = "login";
        String HISTORY_ORDER_ID_ATTRIBUTE = "id";
        String ORDER_ID_ATTRIBUTE = "id";
        String ORDER_DATE_ATTRIBUTE = "dateExpire";
        String BOOK_ID_ATTRIBUTE = "id";
        String PLACE_NAME_ATTRIBUTE = "placeName";
        String PLACE_ID_ATTRIBUTE = "placeId";
        String APP_DISABLED = "disabled";
        String ORDER_STATUS_ATTRIBUTE = "status";
        String APP_CAPTCHA_RESPONSE_ATTRIBUTE = "g-recaptcha-response";
    }

    interface BundleStrings {
        String APP_LOGIN_PATTERN = "pattern.login.regexp";
        String APP_FIRST_NAME_PATTERN = "pattern.eng.name.regexp";
        String APP_FIRST_NAME_PATTERN_UKR = "pattern.ukr.name.regexp";
        String APP_LAST_NAME_PATTERN = "pattern.eng.name.regexp";
        String APP_LAST_NAME_PATTERN_UKR = "pattern.ukr.name.regexp";
        String APP_EMAIL_PATTERN = "pattern.email.regexp";
        String APP_PHONE_PATTERN = "pattern.phone.regexp";
    }

    interface Validation {
        String NAME_PATTERN_ERROR = "form.name.symbols";
        String NAME_LENGTH = "form.text.error.from.three";
        String PASSWORD_ERROR = "error.password";
        String LOGIN_PATTERN_ERROR = "form.login.symbols";
        String EMAIL_PATTERN_ERROR = "form.email.symbols";
        String PHONE_PATTERN_ERROR = "form.phone.symbols";
        String EMAIL_LOGIN_LENGTH = "form.text.error.from.six";
        String PASSWORD_LENGTH = "form.text.error.from.eight";
        String DUPLICATE_FIELD = "error.duplicate";
        String CAPTCHA_ERROR = "error.captcha";
    }

    String PARAMETER_SORT_BY = "sortBy";
    String BUNDLE_ORDER_STATUS_PREFIX = "library.order.";
    String ORDER_DEFAULT_STATUS = "REGISTER";
    String APP_STRINT_DEFAULT_SORTING = "book_name";
    String STATUS_CODE_PARAMETER = "statusCode";
    String STATUS_CODE_REGISTERED = "REGISTER";
    Object APP_DISABLED_USER = "true";
    String DEFAULT_ORDER_SORT_BY = "date_created";
    int DEFAULT_BOOK_COUNT = 1;
    Date DEFAULT_DATE = new Date(0);
    int DEFAULT_FINE = 0;
    long INVALID_ID = -1;
    String APP_UNAUTHORIZED_USER = "anonymous";
    int APP_BCRYPT_SALT = 11;
    String APP_ENCODING = "UTF-8";

    String APP_DEFAULT_LANGUAGE = "en";
    long APP_DEFAULT_ID = -1;
    long APP_DEFAULT_USER_MANIPULATION_ID = 1;
    int DEFAULT_CURRENCY_EXCHANGE = 0;

    String ORDER_STATUS_ACCEPT = "ACCEPT";
    String ORDER_PLACE_READING_ROOM = "To the reading room";

    String BUNDLE_NAME = "interface";
    String APP_PATH_REG_EXP = ".*/library/";
    String APP_STRING_DEFAULT_VALUE = "";
    String APP_ADMIN_ROLE = "ADMIN";
    String[] APP_ROLES = new String[]{
            "ADMIN",
            "LIBRARIAN",
            "USER"};
    String BUNDLE_ACCESS_DENIED_LOGGED_USERS = "access.denied.logged.user";
    String BUNDLE_ACCESS_DENIED_MESSAGE = "access.denied.message";
    String BUNDLE_CURRENCY = "locale.currency";
    String BUNDLE_CURRENCY_EXCHANGE = "locale.currency.exchange";
    String BUNDLE_DEFAULT_IS_CLOSED_MESSAGE = "app.literal.false";
    String BUNDLE_LANGUAGE_FOR_BOOK = "locale.string";
    String BUNDLE_PLACE_PREFIX = "library.place.";
    String APP_DEFAULT_PLACE_NAME = "To the reading room";
    String DEFAULT_USER_ROLE = "USER";
    int DEFAULT_MIN_IDLE = 5;
    int DEFAULT_MAX_IDLE = 10;
    int MYSQL_MAX_POOL_SIZE = 100;
}

package ua.org.training.library.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DefaultValues {
    APP_DEFAULT_LOCALE("en"),
    BUNDLE_NAME("interface"),
    BUNDLE_CURRENCY_EXCHANGE("currency_exchange"),
    APP_UNAUTHORIZED_USER("anonymous"),
    USER_ATTRIBUTE("user"),
    LOGGED_USERS_SET_CONTEXT("loggedUsers"),
    APP_ENCODING("UTF-8"),
    APP_LANG_ATTRIBUTE("lang"),
    LIBRARY_PATH("/library"),
    APP_MESSAGE_ATTRIBUTE("message"),
    BUNDLE_ACCESS_DENIED_MESSAGE("access.denied.message"),
    ROLE_USER("USER"),
    BUNDLE_ORDER_STATUS_NOT_SET("order.status.not.set"),
    BUNDLE_ORDER_STATUS_PREFIX("order.status."),
    CODE_ATTRIBUTE("code"),
    EMAIL_ATTRIBUTE("email"),
    APP_DEFAULT_LANGUAGE("en"),
    REGISTERED("REGISTER"),
    SQL_CONNECTION_INIT("SELECT 1"),;

    private final String value;
}

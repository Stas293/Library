package ua.org.training.library.enums;

public enum Patterns {
    LOGIN_PATTERN("^[a-zA-Zа-яА-Я0-9_-]{6,255}$"),
    PASSWORD_PATTERN("^[a-zA-Z0-9_-]{6,255}$"),
    NAME_PATTERN("^[a-zA-Zа-яА-Я]{3,255}$"),
    EMAIL_PATTERN("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"),
    PHONE_PATTERN("^\\+380[0-9]{9}$"),
    TITLE_PATTERN("^[a-zA-Zа-яА-Я0-9\\s]{3,255}$"),
    DESCRIPTION_PATTERN("^[\\p{L}\\d\\s.,!?-]{3,65535}$"),
    ISBN_PATTERN("^[0-9]{13}$"),
    COUNT_PATTERN("^[1-9][0-9]{0,3}$"),
    FINE_PATTERN("^[0-9]{1,3}(\\.[0-9]{1,2})?$"),
    LOCATION_PATTERN("^[\\p{L}\\d\\s.,!?-]{3,255}$");


    private final String pattern;

    Patterns(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}

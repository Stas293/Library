package ua.org.training.library.enums;

public enum Patterns {
    LOGIN_PATTERN("^[a-zA-Zа-яА-Я0-9_-]{6,255}$"),
    PASSWORD_PATTERN("^[a-zA-Z0-9_-]{6,255}$"),
    NAME_PATTERN("^[a-zA-Zа-яА-Я]{3,255}$"),
    EMAIL_PATTERN("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"),
    PHONE_PATTERN("^\\+380[0-9]{9}$");

    private final String pattern;

    Patterns(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}

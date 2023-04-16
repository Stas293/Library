package ua.org.training.library.enums;

public enum Validation {
    NAME_PATTERN_ERROR("Name must contain only letters and be between 3 and 255 characters"),
    LOGIN_PATTERN_ERROR("Login must contain only letters, numbers, underscores and hyphens and be between 6 and 255 characters"),
    EMAIL_PATTERN_ERROR("Email must be valid"),
    PHONE_PATTERN_ERROR("Phone must be valid"),
    PASSWORD_PATTERN_ERROR("Password must contain only letters and numbers and be between 6 and 255 characters"),
    DUPLICATE_FIELD_ERROR("This field already exists"),
    CAPTCHA_ERROR("Captcha is not valid"),
    EMPTY_PASSWORD("Password is empty"),
    PASSWORD_NOT_MATCH_ERROR("Passwords do not match");
    private final String message;

    Validation(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

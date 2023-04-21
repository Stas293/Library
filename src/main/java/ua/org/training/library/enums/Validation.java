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
    PASSWORD_NOT_MATCH_ERROR("Passwords do not match"),
    INCORRECT_PASSWORD("Incorrect password"),
    TITLE_EMPTY("Title is empty"),
    TITLE_PATTERN_ERROR("Title must contain only letters, numbers and be between 3 and 255 characters"),
    DESCRIPTION_EMPTY("Description is empty"),
    DESCRIPTION_PATTERN_ERROR("Description must contain only letters, numbers, punctuation marks and be between 3 and 65535 characters"),
    ISBN_EMPTY("ISBN is empty"),
    ISBN_PATTERN_ERROR("ISBN must contain only numbers and be 13 characters long"),
    COUNT_EMPTY("Count is empty"),
    COUNT_PATTERN_ERROR("Count must contain only numbers and be between 1 and 1000"),
    PUBLICATION_DATE_AFTER_TODAY("Publication date cannot be after today"),
    FINE_EMPTY("Fine is empty"),
    FINE_PATTERN_ERROR("Fine must contain only numbers and be between 0.01 and 1000"),
    LANGUAGE_EMPTY("Language is empty"),
    LOCATION_EMPTY("Location is empty"),
    LOCATION_PATTERN_ERROR("Location must contain only letters, numbers, punctuation marks and be between 3 and 255 characters"),
    AUTHORS_EMPTY("Authors is empty"),
    KEYWORDS_EMPTY("Keywords is empty"),
    PUBLICATION_DATE_EMPTY("Publication date is empty"),
    ISBN_EXISTS("ISBN already exists"),;
    private final String message;

    Validation(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

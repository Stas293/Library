package ua.org.training.library.enums;

public enum Validation {
    NAME_PATTERN_ERROR("form.validation.name"),
    LOGIN_PATTERN_ERROR("form.validation.login"),
    EMAIL_PATTERN_ERROR("form.validation.email"),
    PHONE_PATTERN_ERROR("form.validation.phone"),
    PASSWORD_PATTERN_ERROR("form.validation.password"),
    DUPLICATE_FIELD_ERROR("form.validation.duplicate"),
    CAPTCHA_ERROR("form.validation.captcha"),
    EMPTY_PASSWORD("form.validation.empty.password"),
    PASSWORD_NOT_MATCH_ERROR("form.validation.password.not.match"),
    INCORRECT_PASSWORD("form.validation.incorrect.password"),
    TITLE_EMPTY("form.validation.title.empty"),
    TITLE_PATTERN_ERROR("form.validation.title.pattern"),
    DESCRIPTION_EMPTY("form.validation.description.empty"),
    DESCRIPTION_PATTERN_ERROR("form.validation.description.pattern"),
    ISBN_EMPTY("form.validation.isbn.empty"),
    ISBN_PATTERN_ERROR("form.validation.isbn.pattern"),
    COUNT_EMPTY("form.validation.count.empty"),
    COUNT_PATTERN_ERROR("form.validation.count.pattern"),
    PUBLICATION_DATE_AFTER_TODAY("form.validation.publication.date.after.today"),
    FINE_EMPTY("form.validation.fine.empty"),
    FINE_PATTERN_ERROR("form.validation.fine.pattern"),
    LANGUAGE_EMPTY("form.validation.language.empty"),
    LOCATION_EMPTY("form.validation.location.empty"),
    LOCATION_PATTERN_ERROR("form.validation.location.pattern"),
    AUTHORS_EMPTY("form.validation.authors.empty"),
    KEYWORDS_EMPTY("form.validation.keywords.empty"),
    PUBLICATION_DATE_EMPTY("form.validation.publication.date.empty"),
    ISBN_EXISTS("form.validation.isbn.exists"),
    ;
    private final String message;

    Validation(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

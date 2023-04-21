package ua.org.training.library.validator;

import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dto.AuthorManagementDto;
import ua.org.training.library.dto.BookChangeDto;
import ua.org.training.library.dto.KeywordManagementDto;
import ua.org.training.library.enums.Patterns;
import ua.org.training.library.enums.Validation;
import ua.org.training.library.form.BookChangeFormValidationError;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
public class BookChangeValidator {
    public BookChangeFormValidationError validate(BookChangeDto bookDto) {
        log.info("Validating book change");
        log.info("Book: {}", bookDto);
        BookChangeFormValidationError errors = new BookChangeFormValidationError();

        validateTitle(
                bookDto.getTitle(),
                Patterns.TITLE_PATTERN.getPattern(),
                errors);

        validateDescription(
                bookDto.getDescription(),
                Patterns.DESCRIPTION_PATTERN.getPattern(),
                errors);

        validateIsbn(
                bookDto.getIsbn(),
                Patterns.ISBN_PATTERN.getPattern(),
                errors);

        validateCount(
                bookDto.getCount(),
                Patterns.COUNT_PATTERN.getPattern(),
                errors);

        validatePublicationDate(
                bookDto.getPublicationDate(),
                errors);

        validateFine(
                bookDto.getFine(),
                Patterns.FINE_PATTERN.getPattern(),
                errors);

        validateLanguage(
                bookDto.getLanguage(),
                errors);

        validateLocation(
                bookDto.getLocation(),
                Patterns.LOCATION_PATTERN.getPattern(),
                errors);

        validateAuthors(
                bookDto.getAuthors(),
                errors);

        validateKeywords(
                bookDto.getKeywords(),
                errors);

        log.info("Errors: {}", errors);
        return errors;
    }

    private void validateKeywords(List<KeywordManagementDto> keywords, BookChangeFormValidationError errors) {
        if (keywords == null || keywords.isEmpty()) {
            errors.setKeywords(Validation.KEYWORDS_EMPTY.getMessage());
        }
    }

    private void validateAuthors(List<AuthorManagementDto> authors, BookChangeFormValidationError errors) {
        if (authors == null || authors.isEmpty()) {
            errors.setAuthors(Validation.AUTHORS_EMPTY.getMessage());
        }
    }

    private void validateLocation(String location, String pattern, BookChangeFormValidationError errors) {
        Pattern locationPattern = Pattern.compile(pattern);
        if (location == null || location.isEmpty()) {
            errors.setLocation(Validation.LOCATION_EMPTY.getMessage());
        } else if (!locationPattern.matcher(location).matches()) {
            errors.setLocation(Validation.LOCATION_PATTERN_ERROR.getMessage());
        }
    }

    private void validateLanguage(String language, BookChangeFormValidationError errors) {
        if (language == null || language.isEmpty()) {
            errors.setLanguage(Validation.LANGUAGE_EMPTY.getMessage());
        }
    }

    private void validateFine(double fine, String pattern, BookChangeFormValidationError errors) {
        Pattern finePattern = Pattern.compile(pattern);
        if (fine == 0) {
            errors.setFine(Validation.FINE_EMPTY.getMessage());
        } else if (!finePattern.matcher(String.valueOf(fine)).matches()) {
            errors.setFine(Validation.FINE_PATTERN_ERROR.getMessage());
        }
    }

    private void validatePublicationDate(LocalDate publicationDate, BookChangeFormValidationError errors) {
        if (publicationDate == null) {
            errors.setPublicationDate(Validation.PUBLICATION_DATE_EMPTY.getMessage());
        } else if (publicationDate.isAfter(LocalDate.now())) {
            errors.setPublicationDate(Validation.PUBLICATION_DATE_AFTER_TODAY.getMessage());
        }
    }

    private void validateCount(long count, String pattern, BookChangeFormValidationError errors) {
        Pattern countPattern = Pattern.compile(pattern);
        if (count == 0) {
            errors.setCount(Validation.COUNT_EMPTY.getMessage());
        } else if (!countPattern.matcher(String.valueOf(count)).matches()) {
            errors.setCount(Validation.COUNT_PATTERN_ERROR.getMessage());
        }
    }

    private void validateIsbn(String isbn, String pattern, BookChangeFormValidationError errors) {
        Pattern isbnPattern = Pattern.compile(pattern);
        if (isbn == null || isbn.isEmpty()) {
            errors.setIsbn(Validation.ISBN_EMPTY.getMessage());
        } else if (!isbnPattern.matcher(isbn).matches()) {
            errors.setIsbn(Validation.ISBN_PATTERN_ERROR.getMessage());
        }
    }

    private void validateDescription(String description, String pattern, BookChangeFormValidationError errors) {
        Pattern descriptionPattern = Pattern.compile(pattern);
        if (description == null || description.isEmpty()) {
            errors.setDescription(Validation.DESCRIPTION_EMPTY.getMessage());
        } else if (!descriptionPattern.matcher(description).matches()) {
            errors.setDescription(Validation.DESCRIPTION_PATTERN_ERROR.getMessage());
        }
    }

    private void validateTitle(String title, String pattern, BookChangeFormValidationError errors) {
        Pattern titlePattern = Pattern.compile(pattern);
        if (title == null || title.isEmpty()) {
            errors.setTitle(Validation.TITLE_EMPTY.getMessage());
        } else if (!titlePattern.matcher(title).matches()) {
            errors.setTitle(Validation.TITLE_PATTERN_ERROR.getMessage());
        }
    }
}

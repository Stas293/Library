package ua.org.training.library.validator;

import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dto.AuthorManagementDto;
import ua.org.training.library.enums.Patterns;
import ua.org.training.library.enums.Validation;
import ua.org.training.library.form.AuthorSaveFormValidationError;

import java.util.regex.Pattern;

@Slf4j
@Component
public class AuthorSaveValidator {
    public AuthorSaveFormValidationError validate(AuthorManagementDto authorDto) {
        log.info("Validating author save");
        log.info("Author: {}", authorDto);
        AuthorSaveFormValidationError errors = new AuthorSaveFormValidationError();

        validateFirstName(
                authorDto.getFirstName(),
                Patterns.NAME_PATTERN.getPattern(),
                errors);

        validateMiddleName(
                authorDto.getMiddleName(),
                Patterns.NAME_PATTERN.getPattern(),
                errors);

        validateLastName(
                authorDto.getLastName(),
                Patterns.NAME_PATTERN.getPattern(),
                errors);

        log.info("Errors: {}", errors);
        return errors;
    }

    private void validateLastName(String lastName, String pattern, AuthorSaveFormValidationError errors) {
        Pattern p = Pattern.compile(pattern);
        if (!p.matcher(lastName).matches()) {
            errors.setLastName(Validation.NAME_PATTERN_ERROR.getMessage());
        }
    }

    private void validateMiddleName(String middleName, String pattern, AuthorSaveFormValidationError errors) {
        if (middleName == null) {
            return;
        }
        Pattern p = Pattern.compile(pattern);
        if (!p.matcher(middleName).matches()) {
            errors.setMiddleName(Validation.NAME_PATTERN_ERROR.getMessage());
        }
    }

    private void validateFirstName(String firstName, String pattern, AuthorSaveFormValidationError errors) {
        Pattern p = Pattern.compile(pattern);
        if (!p.matcher(firstName).matches()) {
            errors.setFirstName(Validation.NAME_PATTERN_ERROR.getMessage());
        }
    }
}

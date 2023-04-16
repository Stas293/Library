package ua.org.training.library.validator;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dto.UserRegistrationDto;
import ua.org.training.library.enums.Patterns;
import ua.org.training.library.enums.Validation;
import ua.org.training.library.form.RegistrationFormValidation;

import java.util.regex.Pattern;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserRegistrationValidator {
    private final CaptchaValidator captchaValidator;

    public RegistrationFormValidation validation(UserRegistrationDto user) {
        RegistrationFormValidation errors = new RegistrationFormValidation();
        validateLoginPattern(
                user.getLogin(),
                Patterns.LOGIN_PATTERN.getPattern(),
                errors);

        validateFirstNamePattern(
                user.getFirstName(),
                Patterns.NAME_PATTERN.getPattern(),
                errors);

        validateLastNamePattern(
                user.getLastName(),
                Patterns.NAME_PATTERN.getPattern(),
                errors);

        validateEmailPattern(
                user.getEmail(),
                Patterns.EMAIL_PATTERN.getPattern(),
                errors);

        validatePhonePattern(
                user.getPhone(),
                Patterns.PHONE_PATTERN.getPattern(),
                errors);

        validatePasswordPattern(
                user.getPassword(),
                Patterns.PASSWORD_PATTERN.getPattern(),
                errors);
        validateConfirmPassword(
                user.getPassword(),
                user.getConfirmPassword(),
                errors);

        validateCaptcha(
                user.getCaptcha(),
                errors);

        return errors;
    }

    private void validateLastNamePattern(String lastName, String pattern, RegistrationFormValidation errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(lastName).matches())
            errors.setLastName(Validation.NAME_PATTERN_ERROR.getMessage());
    }

    private void validateFirstNamePattern(String firstName, String pattern, RegistrationFormValidation errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(firstName).matches())
            errors.setFirstName(Validation.NAME_PATTERN_ERROR.getMessage());
    }

    public void validateConfirmPassword(String password, String confirmPassword, RegistrationFormValidation errors) {
        if (!password.equals(confirmPassword))
            errors.setPassword(Validation.PASSWORD_NOT_MATCH_ERROR.getMessage());
    }

    private void validateLoginPattern(String login, String pattern, RegistrationFormValidation errors) {
        Pattern r = Pattern.compile(pattern);
        log.info(String.format("Login: %s, pattern: %s", login, pattern));
        if (!r.matcher(login).matches())
            errors.setLogin(Validation.LOGIN_PATTERN_ERROR.getMessage());
    }

    private void validateEmailPattern(String email, String pattern, RegistrationFormValidation errors) {
        Pattern r = Pattern.compile(pattern);
        log.info(String.format("email: %s, pattern: %s", email, pattern));
        if (!r.matcher(email).matches())
            errors.setEmail(Validation.EMAIL_PATTERN_ERROR.getMessage());
    }

    private void validatePhonePattern(String phone, String pattern, RegistrationFormValidation errors) {
        Pattern r = Pattern.compile(pattern);
        log.info(String.format("phone: %s, pattern: %s", phone, pattern));
        if (!r.matcher(phone).matches())
            errors.setPhone(Validation.PHONE_PATTERN_ERROR.getMessage());
    }

    private void validatePasswordPattern(String password, String pattern, RegistrationFormValidation errors) {
        Pattern p = Pattern.compile(pattern);
        if (!p.matcher(password).matches())
            errors.setPassword(Validation.PASSWORD_PATTERN_ERROR.getMessage());
    }

    public void validateCaptcha(String captchaResponse, RegistrationFormValidation formErrors) {
        if (captchaValidator.checkCaptcha(captchaResponse)) {
            return;
        }
        formErrors.setCaptcha(Validation.CAPTCHA_ERROR.getMessage());
    }
}

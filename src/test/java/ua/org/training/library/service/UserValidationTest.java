package ua.org.training.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.exceptions.UnexpectedValidationException;
import ua.org.training.library.form.FormValidationError;
import ua.org.training.library.utility.validation.UserValidation;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserValidationTest {
    @Mock
    private UserService userService;
    private UserValidation userValidation;
    private FormValidationError formValidationError;

    @BeforeEach
    public void init() {
        userValidation = new UserValidation(userService);
        formValidationError = new FormValidationError();
    }

    @Test
    void validation() throws ServiceException, UnexpectedValidationException, ConnectionDBException {
        User user = User.builder()
                .setLogin("1ogin")
                .setEmail("email")
                .setPhone("phone")
                .setFirstName("fi")
                .setLastName("la")
                .createUser();
        Mockito.when(userService.getUserByLogin(Mockito.anyString())).thenReturn(
                User.builder()
                        .setId(0)
                        .createUser()
        );
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(
                User.builder()
                        .setEmail("some_email")
                        .createUser()
        );
        Mockito.when(userService.getUserByPhone(Mockito.anyString())).thenReturn(
                User.builder()
                        .setPhone("some_phone")
                        .createUser()
        );
        userValidation.validation(Locale.ENGLISH, user, formValidationError);
        assertTrue(formValidationError.isContainsErrors());
        assertFalse(formValidationError.getLogin().isEmpty());
        assertFalse(formValidationError.getEmail().isEmpty());
        assertFalse(formValidationError.getPhone().isEmpty());
        assertFalse(formValidationError.getFirstName().isEmpty());
        assertFalse(formValidationError.getLastName().isEmpty());

        user.setLogin("User_login");
        Mockito.when(userService.getUserByLogin(user.getLogin())).thenThrow(ServiceException.class);
        formValidationError = new FormValidationError();
        userValidation.validation(Locale.ENGLISH, user, formValidationError);
        assertNull(formValidationError.getLogin());

        user.setEmail("usermail@gmail.com");
        Mockito.when(userService.getUserByEmail(user.getEmail())).thenThrow(ServiceException.class);
        formValidationError = new FormValidationError();
        userValidation.validation(Locale.ENGLISH, user, formValidationError);
        assertNull(formValidationError.getEmail());

        user.setPhone("+380501234567");
        Mockito.when(userService.getUserByPhone(user.getPhone())).thenThrow(ServiceException.class);
        formValidationError = new FormValidationError();
        userValidation.validation(Locale.ENGLISH, user, formValidationError);
        assertNull(formValidationError.getPhone());

        user.setFirstName("Alex");
        formValidationError = new FormValidationError();
        userValidation.validation(Locale.ENGLISH, user, formValidationError);
        assertNull(formValidationError.getFirstName());

        user.setLastName("Smith");
        formValidationError = new FormValidationError();
        userValidation.validation(Locale.ENGLISH, user, formValidationError);
        assertNull(formValidationError.getLastName());

        assertFalse(formValidationError.isContainsErrors());
    }

    @Test
    void validateConfirmPassword() {
        userValidation.validateConfirmPassword("pass", "pass", formValidationError);
        assertNull(formValidationError.getPassword());

        userValidation.validateConfirmPassword("password", "password", formValidationError);
        assertNull(formValidationError.getPassword());

        userValidation.validateConfirmPassword("password", "password1", formValidationError);
        assertNotNull(formValidationError.getPassword());
    }

    @Test
    void validatePasswordLength() {
        userValidation.validatePasswordLength("pass", formValidationError);
        assertNotNull(formValidationError.getPassword());

        formValidationError = new FormValidationError();

        userValidation.validatePasswordLength("password", formValidationError);
        assertNull(formValidationError.getPassword());

        userValidation.validatePasswordLength("pass", formValidationError);
        assertNotNull(formValidationError.getPassword());
    }
}
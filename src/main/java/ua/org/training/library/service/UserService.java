package ua.org.training.library.service;


import ua.org.training.library.dto.*;
import ua.org.training.library.form.PersonalEditFormValidationError;
import ua.org.training.library.form.RegistrationFormValidation;
import ua.org.training.library.form.ResetValidationError;
import ua.org.training.library.model.User;
import ua.org.training.library.security.AuthorityUser;

import java.util.Optional;

public interface UserService extends GenericService<Long, User> {
    RegistrationFormValidation save(UserRegistrationDto user);

    boolean login(String login, String password);

    Optional<UserDto> getByLogin(String username);

    Optional<UserDto> getByEmail(String email);

    Optional<UserDto> getByPhone(String phone);

    void disable(User user);

    void enable(User user);

    void updateRolesForUser(User user);

    ResetValidationError updatePassword(UserChangePasswordDto userChangePasswordDto);

    PersonalEditFormValidationError updatePersonalData(UserUpdateDto userFromRequest, AuthorityUser authorityUser);

    Optional<AuthorityUser> logUser(UserLoginDto userLoginDto);

    boolean checkUserByEmail(User user, String email);

    boolean checkUserByPhone(User user, String phone);
}

package ua.org.training.library.service;


import ua.org.training.library.dto.*;
import ua.org.training.library.form.LoggedUserUpdatePasswordFormValidationError;
import ua.org.training.library.form.PersonalEditFormValidationError;
import ua.org.training.library.form.RegistrationFormValidationError;
import ua.org.training.library.form.ResetValidationError;
import ua.org.training.library.model.User;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Optional;

public interface UserService {
    RegistrationFormValidationError save(UserRegistrationDto user);

    boolean login(String login, String password);

    Optional<UserDto> getByLogin(String username);

    Optional<UserDto> getByEmail(String email);

    Optional<UserDto> getByPhone(String phone);

    Optional<UserDto> disable(Long user);

    Optional<UserDto> enable(Long user);

    Optional<UserDto> updateRolesForUser(UserChangeRolesDto user);

    ResetValidationError updatePassword(UserChangePasswordDto userChangePasswordDto);

    PersonalEditFormValidationError updatePersonalData(UserUpdateDto userFromRequest, AuthorityUser authorityUser);

    Optional<AuthorityUser> logUser(UserLoginDto userLoginDto);

    boolean checkUserByEmail(User user, String email);

    boolean checkUserByPhone(User user, String phone);

    Page<UserDto> searchUsers(Pageable pageable, String search);

    Optional<UserManagementDto> getUserManagementDtoById(Long id);

    Optional<UserDto> deleteUserById(Long id);

    LoggedUserUpdatePasswordFormValidationError updatePassword(UserLoggedUpdatePasswordDto userFromRequest,
                                                               AuthorityUser authorityUser);
}

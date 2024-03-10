package ua.org.training.library.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.library.enums.DefaultValues;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.dto.*;
import ua.org.training.library.enums.Validation;
import ua.org.training.library.form.LoggedUserUpdatePasswordFormValidationError;
import ua.org.training.library.form.PersonalEditFormValidationError;
import ua.org.training.library.form.RegistrationFormValidationError;
import ua.org.training.library.form.ResetValidationError;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.RoleRepository;
import ua.org.training.library.repository.UserRepository;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.validator.LoggedUserEditPasswordValidator;
import ua.org.training.library.validator.ResetPasswordValidator;
import ua.org.training.library.validator.UserEditPersonalValidator;
import ua.org.training.library.validator.UserRegistrationValidator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;
    private final UserEditPersonalValidator userEditPersonalValidator;
    private final UserRegistrationValidator userRegistrationValidator;
    private final ResetPasswordValidator resetPasswordValidator;
    private final LoggedUserEditPasswordValidator loggedUserEditPasswordValidator;
    private static final int APP_BCRYPT_SALT = 10;

    @Override
    @Transactional
    public RegistrationFormValidationError save(UserRegistrationDto userRegistrationDto) {
        log.info("Saving userRegistrationDto: {}", userRegistrationDto);
        RegistrationFormValidationError registrationFormValidationError = userRegistrationValidator.validation(userRegistrationDto);
        validateEmail(userRegistrationDto.getEmail(), registrationFormValidationError);
        validateLogin(userRegistrationDto.getLogin(), registrationFormValidationError);
        validatePhone(userRegistrationDto.getPhone(), registrationFormValidationError);
        if (registrationFormValidationError.isContainsErrors()) {
            return registrationFormValidationError;
        }
        String bcryptPassword = BCrypt.hashpw(
                userRegistrationDto.getPassword(), BCrypt.gensalt(APP_BCRYPT_SALT));
        User user = objectMapper.map(userRegistrationDto);
        user.setEnabled(true);
        User newUser = userRepository.save(user, bcryptPassword);
        List<Role> roles = Collections.singletonList(
                roleRepository.findByCode(DefaultValues.ROLE_USER.getValue()).orElseThrow());
        newUser.setRoles(roles);
        userRepository.updateRolesForUser(newUser);
        return registrationFormValidationError;
    }

    private void validateLogin(String login, RegistrationFormValidationError errors) {
        if (getByLogin(login).isEmpty())
            return;
        errors.setLogin(Validation.DUPLICATE_FIELD_ERROR.getMessage());
        log.info(String.format("Login: %s already exists", login));
    }

    private void validateEmail(String email, RegistrationFormValidationError errors) {
        if (getByEmail(email).isEmpty())
            return;
        log.debug(String.format("Email: %s already exists", email));
        errors.setEmail(Validation.DUPLICATE_FIELD_ERROR.getMessage());
    }

    private void validatePhone(String phone, RegistrationFormValidationError errors) {
        if (getByPhone(phone).isEmpty())
            return;
        log.debug(String.format("Phone: %s already exists", phone));
        errors.setPhone(Validation.DUPLICATE_FIELD_ERROR.getMessage());
    }

    @Override
    public boolean login(String login, String password) {
        log.info("Login: " + login);
        Optional<User> user = userRepository.getByLogin(login);
        if (user.isEmpty()) {
            return false;
        }
        String correctPassword = userRepository.getPasswordForUser(user.get());
        return BCrypt.checkpw(password, correctPassword);
    }

    @Override
    public Optional<UserDto> getByLogin(String username) {
        log.info("Getting user by login: {}", username);
        Optional<User> user = userRepository.getByLogin(username);
        return user.map(objectMapper::mapUserToUserDto);
    }

    @Override
    public Optional<UserDto> getByEmail(String email) {
        log.info("Getting user by email: {}", email);
        Optional<User> user = userRepository.getByEmail(email);
        return user.map(objectMapper::mapUserToUserDto);
    }

    @Override
    public Optional<UserDto> getByPhone(String phone) {
        log.info("Getting user by phone: {}", phone);
        Optional<User> user = userRepository.getByPhone(phone);
        return user.map(objectMapper::mapUserToUserDto);
    }

    @Override
    @Transactional
    public Optional<UserDto> disable(Long user) {
        log.info("Disabling user: {}", user);
        User userFromDb = userRepository.findById(user).orElseThrow();
        userFromDb.setEnabled(false);
        userRepository.disable(userFromDb);
        return Optional.of(objectMapper.mapUserToUserDto(userFromDb));
    }

    @Override
    @Transactional
    public Optional<UserDto> enable(Long user) {
        log.info("Enabling user: {}", user);
        User userFromDb = userRepository.findById(user).orElseThrow();
        userFromDb.setEnabled(true);
        userRepository.enable(userFromDb);
        return Optional.of(objectMapper.mapUserToUserDto(userFromDb));
    }

    @Override
    @Transactional
    public Optional<UserDto> updateRolesForUser(UserChangeRolesDto user) {
        log.info("Updating roles for user: {}", user);
        User userFromDb = userRepository.findById(user.getId()).orElseThrow();
        List<Role> roles = roleRepository.findAllByCodes(user.getRoles());
        userFromDb.setRoles(roles);
        userRepository.updateRolesForUser(userFromDb);
        return Optional.of(objectMapper.mapUserToUserDto(userFromDb));
    }

    @Override
    @Transactional
    public ResetValidationError updatePassword(UserChangePasswordDto userChangePasswordDto) {
        log.info("Updating password for user: {}", userChangePasswordDto);
        ResetValidationError resetValidationError = resetPasswordValidator.validate(userChangePasswordDto);
        if (resetValidationError.isContainsErrors()) {
            return resetValidationError;
        }
        User user = userRepository.getByLogin(userChangePasswordDto.getLogin()).orElseThrow();
        String bcryptPassword = BCrypt.hashpw(
                userChangePasswordDto.getNewPassword(), BCrypt.gensalt(APP_BCRYPT_SALT));
        userRepository.updatePassword(user, bcryptPassword);
        return null;
    }

    @Override
    @Transactional
    public PersonalEditFormValidationError updatePersonalData(UserUpdateDto userFromRequest,
                                                              AuthorityUser authorityUser) {
        log.info("Updating personal data for user: {}", userFromRequest);
        User user = userRepository.getByLogin(authorityUser.getLogin()).orElseThrow();
        PersonalEditFormValidationError validationError = userEditPersonalValidator.validation(userFromRequest);
        validateEmail(userFromRequest.getEmail(), validationError, user);
        validatePhone(userFromRequest.getPhone(), validationError, user);
        if (validationError.isContainsErrors()) {
            log.info("Validation error: {}", validationError);
            return validationError;
        }
        if (checkIfPersonalDataChanged(user, userFromRequest)) {
            log.info("Personal data changed for user: {}", userFromRequest);
            User updatedUser = objectMapper.updateUserData(user, userFromRequest);
            userRepository.save(updatedUser);
        }
        return validationError;
    }

    private boolean checkIfPersonalDataChanged(User user, UserUpdateDto updatedUser) {
        return !user.getFirstName().equals(updatedUser.getFirstName())
                || !user.getLastName().equals(updatedUser.getLastName())
                || !user.getEmail().equals(updatedUser.getEmail())
                || !user.getPhone().equals(updatedUser.getPhone());
    }

    private void validateEmail(String email, PersonalEditFormValidationError errors, User user) {
        if (!checkUserByEmail(user, email)) {
            errors.setEmail(Validation.DUPLICATE_FIELD_ERROR.getMessage());
        }
    }

    private void validatePhone(String phone, PersonalEditFormValidationError errors, User user) {
        if (!checkUserByPhone(user, phone)) {
            errors.setPhone(Validation.DUPLICATE_FIELD_ERROR.getMessage());
        }
    }

    @Override
    public Optional<AuthorityUser> logUser(UserLoginDto userLoginDto) {
        log.info("Logging user: {}", userLoginDto);
        Optional<User> user = userRepository.getByLogin(userLoginDto.getLogin());
        if (user.isEmpty()) {
            return Optional.empty();
        }
        String correctPassword = userRepository.getPasswordForUser(user.get());
        if (!BCrypt.checkpw(userLoginDto.getPassword(), correctPassword)) {
            return Optional.empty();
        }
        return Optional.of(objectMapper.mapUserToAuthorityUser(user.get()));
    }

    @Override
    public boolean checkUserByEmail(User user, String email) {
        log.info("Checking user by email: {}", email);
        Optional<User> userByEmail = userRepository.getByEmail(email);
        return userByEmail.isEmpty() || userByEmail.get().getId().equals(user.getId());
    }

    @Override
    public boolean checkUserByPhone(User user, String phone) {
        log.info("Checking user by phone: {}", phone);
        Optional<User> userByPhone = userRepository.getByPhone(phone);
        return userByPhone.isEmpty() || userByPhone.get().getId().equals(user.getId());
    }

    @Override
    public Page<UserDto> searchUsers(Pageable pageable, String search) {
        log.info("Searching users by: {}", search);
        if (search != null && !search.isEmpty()) {
            Page<User> userPage = userRepository.searchUsers(pageable, search);
            return objectMapper.mapUserPageToUserDtoPage(userPage);
        }
        Page<User> userPage = userRepository.findAll(pageable);
        return objectMapper.mapUserPageToUserDtoPage(userPage);
    }

    @Override
    public Optional<UserManagementDto> getUserManagementDtoById(Long id) {
        log.info("Getting user management dto by id: {}", id);
        Optional<User> user = userRepository.findById(id);
        return user.map(objectMapper::mapUserToUserManagementDto);
    }

    @Override
    @Transactional
    public Optional<UserDto> deleteUserById(Long id) {
        log.info("Deleting user by id: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        userRepository.delete(user.get());
        return Optional.of(objectMapper.mapUserToUserDto(user.get()));
    }

    @Override
    @Transactional
    public LoggedUserUpdatePasswordFormValidationError updatePassword(UserLoggedUpdatePasswordDto userFromRequest,
                                                                      AuthorityUser authorityUser) {
        log.info("Updating password for user: {}", userFromRequest);
        LoggedUserUpdatePasswordFormValidationError validationError = loggedUserEditPasswordValidator.validate(userFromRequest);
        User user = userRepository.getByLogin(authorityUser.getLogin()).orElseThrow();
        validateOldPassword(userFromRequest, validationError, user);
        if (validationError.isContainsErrors()) {
            log.info("Validation error: {}", validationError);
            return validationError;
        }
        if (userFromRequest.getPassword().equals(userFromRequest.getOldPassword())) {
            return validationError;
        }
        String bcryptPassword = BCrypt.hashpw(
                userFromRequest.getPassword(), BCrypt.gensalt(APP_BCRYPT_SALT));
        userRepository.updatePassword(user, bcryptPassword);
        return validationError;
    }

    private void validateOldPassword(UserLoggedUpdatePasswordDto userFromRequest,
                                     LoggedUserUpdatePasswordFormValidationError validationError,
                                     User user) {
        String correctPassword = userRepository.getPasswordForUser(user);
        if (!BCrypt.checkpw(userFromRequest.getOldPassword(), correctPassword)) {
            validationError.setOldPassword(Validation.INCORRECT_PASSWORD.getMessage());
        }
    }
}

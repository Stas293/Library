package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.library.constants.Values;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.dto.*;
import ua.org.training.library.enums.Validation;
import ua.org.training.library.form.PersonalEditFormValidationError;
import ua.org.training.library.form.RegistrationFormValidation;
import ua.org.training.library.form.ResetValidationError;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.RoleRepository;
import ua.org.training.library.repository.UserRepository;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.validator.ResetPasswordValidator;
import ua.org.training.library.validator.UserEditPersonalValidator;
import ua.org.training.library.validator.UserRegistrationValidator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;
    private final UserEditPersonalValidator userEditPersonalValidator;
    private final UserRegistrationValidator userRegistrationValidator;
    private final ResetPasswordValidator resetPasswordValidator;

    @Override
    public User createModel(User model) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public void updateModel(User model) {
        log.info("Updating user: {}", model);
        User userFromDb = userRepository.getByLogin(model.getLogin()).orElseThrow();
        model.setId(userFromDb.getId());
        userRepository.save(model);
    }

    @Override
    @Transactional
    public void deleteModel(User author) {
        log.info("Deleting user: {}", author);
        userRepository.delete(author);
    }

    @Override
    public void createModels(List<User> models) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public void updateModels(List<User> models) {
        log.info("Updating users: {}", models);
        userRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void deleteModels(List<User> models) {
        log.info("Deleting users: {}", models);
        userRepository.deleteAll(models);
    }

    @Override
    public List<User> getAllModels() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    @Override
    public List<User> getModelsByIds(List<Long> ids) {
        log.info("Getting users by ids: {}", ids);
        return userRepository.findAllById(ids);
    }

    @Override
    public long countModels() {
        log.info("Counting users");
        return userRepository.count();
    }

    @Override
    public void deleteAllModels() {
        log.info("Deleting all users");
        userRepository.deleteAll();
    }

    @Override
    public boolean checkIfExists(User model) {
        log.info("Checking if user exists: {}", model);
        return userRepository.existsById(model.getId());
    }

    @Override
    public Page<User> getModelsByPage(int pageNumber, int pageSize) {
        log.info("Getting users by page: {}, {}", pageNumber, pageSize);
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    @Transactional
    public void deleteModelById(Long id) {
        log.info("Deleting user by id: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteModelsByIds(List<Long> ids) {
        log.info("Deleting users by ids: {}", ids);
        userRepository.deleteAllById(ids);
    }

    @Override
    public List<User> getAllModels(String sortField, String sortOrder) {
        log.info("Getting all users by sort: {}, {}", sortField, sortOrder);
        return userRepository.findAll(Sort.by(Sort.Direction.fromString(sortOrder), sortField));
    }

    @Override
    public Page<User> getModelsByPage(int pageNumber, int pageSize, Sort.Direction direction, String... sortField) {
        log.info("Getting users by page: {}, {}, {}, {}", pageNumber, pageSize, direction, sortField);
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize, direction, sortField));
    }

    @Override
    @Transactional
    public RegistrationFormValidation save(UserRegistrationDto userRegistrationDto) {
        log.info("Saving userRegistrationDto: {}", userRegistrationDto);
        RegistrationFormValidation registrationFormValidation = userRegistrationValidator.validation(userRegistrationDto);
        validateEmail(userRegistrationDto.getEmail(), registrationFormValidation);
        validateLogin(userRegistrationDto.getLogin(), registrationFormValidation);
        validatePhone(userRegistrationDto.getPhone(), registrationFormValidation);
        if (registrationFormValidation.containsErrors()) {
            return registrationFormValidation;
        }
        String bcryptPassword = BCrypt.hashpw(
                userRegistrationDto.getPassword(), BCrypt.gensalt(Values.APP_BCRYPT_SALT));
        User user = objectMapper.map(userRegistrationDto);
        user.setEnabled(true);
        User newUser = userRepository.save(user, bcryptPassword);
        List<Role> roles = Collections.singletonList(
                roleRepository.findByCode(Values.ROLE_USER).orElseThrow());
        newUser.setRoles(roles);
        userRepository.updateRolesForUser(newUser);
        return registrationFormValidation;
    }

    private void validateLogin(String login, RegistrationFormValidation errors) {
        if (getByLogin(login).isEmpty())
            return;
        errors.setLogin(Validation.DUPLICATE_FIELD_ERROR.getMessage());
        log.info(String.format("Login: %s already exists", login));
    }

    private void validateEmail(String email, RegistrationFormValidation errors) {
        if (getByEmail(email).isEmpty())
            return;
        log.debug(String.format("Email: %s already exists", email));
        errors.setEmail(Validation.DUPLICATE_FIELD_ERROR.getMessage());
    }

    private void validatePhone(String phone, RegistrationFormValidation errors) {
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
    public void disable(User user) {
        log.info("Disabling user: {}", user);
        userRepository.disable(user);
    }

    @Override
    @Transactional
    public void enable(User user) {
        log.info("Enabling user: {}", user);
        userRepository.enable(user);
    }

    @Override
    @Transactional
    public void updateRolesForUser(User user) {
        log.info("Updating roles for user: {}", user);
        userRepository.updateRolesForUser(user);
    }

    @Override
    @Transactional
    public ResetValidationError updatePassword(UserChangePasswordDto userChangePasswordDto) {
        log.info("Updating password for user: {}", userChangePasswordDto);
        ResetValidationError resetValidationError = resetPasswordValidator.validate(userChangePasswordDto);
        if (resetValidationError.containsErrors()) {
            return resetValidationError;
        }
        User user = userRepository.getByLogin(userChangePasswordDto.getLogin()).orElseThrow();
        String bcryptPassword = BCrypt.hashpw(
                userChangePasswordDto.getNewPassword(), BCrypt.gensalt(Values.APP_BCRYPT_SALT));
        userRepository.updatePassword(user, bcryptPassword);
        return null;
    }

    @Override
    public PersonalEditFormValidationError updatePersonalData(UserUpdateDto userFromRequest, AuthorityUser authorityUser) {
        User user = userRepository.getByLogin(authorityUser.getLogin()).orElseThrow();
        User updatedUser = objectMapper.updateUserData(user, userFromRequest);
        PersonalEditFormValidationError validationError = userEditPersonalValidator.validation(updatedUser);
        validateEmail(userFromRequest.getEmail(), validationError, user);
        validatePhone(userFromRequest.getPhone(), validationError, user);
        if (validationError.isContainsErrors()) {
            return validationError;
        }
        userRepository.save(updatedUser);
        return validationError;
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
}

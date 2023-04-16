package ua.org.training.library.repository.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.connections.TransactionManager;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dao.RoleDao;
import ua.org.training.library.dao.SecurityDao;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.UserRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryImpl implements UserRepository {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final SecurityDao securityDao;
    private final TransactionManager transactionManager;

    @Override
    public User save(User user, String password) {
        log.info("Saving user: {}", user);
        if (user.getId() == null) {
            log.info("Creating user: {}", user);
            user = userDao.create(transactionManager.getConnection(), user, password);
        } else {
            log.info("Updating user: {}", user);
            userDao.update(transactionManager.getConnection(), user);
        }
        return user;
    }

    @Override
    public Optional<User> getByLogin(String username) {
        log.info("Getting user by login: {}", username);
        Optional<User> user = userDao.getByLogin(transactionManager.getConnection(), username);
        user.ifPresent(value ->
                value.setRoles(
                        roleDao.getRolesByUserId(transactionManager.getConnection(),
                                value.getId())));
        return user;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        log.info("Getting user by email: {}", email);
        Optional<User> user = userDao.getByEmail(transactionManager.getConnection(), email);
        user.ifPresent(value ->
                value.setRoles(
                        roleDao.getRolesByUserId(transactionManager.getConnection(),
                                value.getId())));
        return user;
    }

    @Override
    public Optional<User> getByPhone(String phone) {
        log.info("Getting user by phone: {}", phone);
        Optional<User> user = userDao.getByPhone(transactionManager.getConnection(), phone);
        user.ifPresent(value ->
                value.setRoles(
                        roleDao.getRolesByUserId(transactionManager.getConnection(),
                                value.getId())));
        return user;
    }

    @Override
    public void disable(User user) {
        log.info("Disabling user: {}", user);
        userDao.disable(transactionManager.getConnection(), user.getId());
    }

    @Override
    public void enable(User user) {
        log.info("Enabling user: {}", user);
        userDao.enable(transactionManager.getConnection(), user.getId());
    }

    @Override
    public void updateRolesForUser(User user) {
        log.info("Updating roles for user: {}", user);
        userDao.updateRolesByUserId(transactionManager.getConnection(), user.getId(), user.getRoles());
    }

    @Override
    public void updatePassword(User user, String password) {
        log.info("Updating password for user: {}", user);
        userDao.update(transactionManager.getConnection(), user, password);
    }

    @Override
    public String getPasswordForUser(User user) {
        log.info("Getting password for user: {}", user);
        return securityDao.getPasswordByLogin(transactionManager.getConnection(), user.getLogin());
    }

    @Override
    public User save(User entity) {
        if (entity.getId() == null) {
            throw new UnsupportedOperationException("Cannot save user without password");
        } else {
            log.info("Updating user: {}", entity);
            userDao.update(transactionManager.getConnection(), entity);
        }
        return entity;
    }

    @Override
    public List<User> saveAll(List<User> entities) {
        throw new UnsupportedOperationException("Cannot save users without password");
    }

    @Override
    public Optional<User> findById(Long aLong) {
        log.info("Getting user by id: {}", aLong);
        Optional<User> user = userDao.getById(transactionManager.getConnection(), aLong);
        user.ifPresent(value ->
                value.setRoles(
                        roleDao.getRolesByUserId(transactionManager.getConnection(),
                                value.getId())));
        return user;
    }

    @Override
    public boolean existsById(Long aLong) {
        log.info("Checking if user exists by id: {}", aLong);
        return userDao.getById(transactionManager.getConnection(), aLong).isPresent();
    }

    @Override
    public List<User> findAll() {
        log.info("Getting all users");
        return userDao.getAll(transactionManager.getConnection());
    }

    @Override
    public List<User> findAllById(List<Long> longs) {
        log.info("Getting users by ids: {}", longs);
        return userDao.getByIds(transactionManager.getConnection(), longs);
    }

    @Override
    public long count() {
        log.info("Counting users");
        return userDao.count(transactionManager.getConnection());
    }

    @Override
    public void deleteById(Long aLong) {
        log.info("Deleting user by id: {}", aLong);
        userDao.delete(transactionManager.getConnection(), aLong);
    }

    @Override
    public void delete(User entity) {
        log.info("Deleting user: {}", entity);
        userDao.delete(transactionManager.getConnection(), entity.getId());
    }

    @Override
    public void deleteAllById(List<? extends Long> ids) {
        log.info("Deleting users by ids: {}", ids);
        userDao.deleteAll(transactionManager.getConnection(), ids);
    }

    @Override
    public void deleteAll(List<? extends User> entities) {
        log.info("Deleting users: {}", entities);
        List<Long> ids = entities.stream().map(User::getId).toList();
        userDao.deleteAll(transactionManager.getConnection(), ids);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all users");
        userDao.deleteAll(transactionManager.getConnection());
    }

    @Override
    public List<User> findAll(Sort sort) {
        log.info("Getting all users by sort: {}", sort);
        return userDao.getAll(transactionManager.getConnection(), sort);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        log.info("Getting all users by pageable: {}", pageable);
        return userDao.getPage(transactionManager.getConnection(), pageable);
    }
}

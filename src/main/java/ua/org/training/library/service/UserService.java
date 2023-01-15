package ua.org.training.library.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.RoleDao;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.dto.UserManagementDTO;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.DTOMapper;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private final DaoFactory daoFactory;

    public UserService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public long createUser(User user, String password) throws ServiceException, ConnectionDBException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.create(user, password);
        } catch (SQLException | JDBCException e) {
            LOGGER.error(String.format("Error while creating user: %s", user), e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while creating user: %s", user), e);
            throw new ServiceException("Error while creating user", e);
        }
    }

    public User getUserById(long id) throws ServiceException, ConnectionDBException {
        User user;
        try (UserDao userDao = daoFactory.createUserDao()) {
            user = userDao.getById(id).orElseThrow(() -> new ServiceException("User not found"));
            addRolesForUser(user);
            return user;
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while getting user by id: %d", id), e);
            throw new ServiceException("Error while getting user by id", e);
        }
    }

    public User getUserByLogin(String login) throws ServiceException, ConnectionDBException {
        User user;
        try (UserDao userDao = daoFactory.createUserDao()) {
            user = userDao.getByLogin(login).orElseThrow(() -> new ServiceException("User not found"));
            return addRolesForUser(user);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while getting user by login: %s", login), e);
            throw new ServiceException("Error while getting user by login", e);
        }
    }

    public User getUserByEmail(String email) throws ServiceException, ConnectionDBException {
        User user;
        try (UserDao userDao = daoFactory.createUserDao()) {
            user = userDao.getByEmail(email).orElseThrow(() -> new ServiceException("User not found"));
            return addRolesForUser(user);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while getting user by email: %s", email), e);
            throw new ServiceException("Error while getting user by email", e);
        }
    }

    private User addRolesForUser(User user) throws ServiceException, JDBCException {
        try (RoleDao roleDao = daoFactory.createRoleDao()) {
            List<Role> roles;
            if (user.getId() != null) {
                roles = roleDao.getRolesByUserId(user.getId());
            } else if (user.getLogin() != null) {
                roles = roleDao.getRolesByUserLogin(user.getLogin());
            } else {
                throw new ServiceException("User has no id or login");
            }
            if (roles.isEmpty()) {
                throw new ServiceException("User has no roles");
            }
            user.setRoles(roles);
        }
        return user;
    }

    public User getUserByPhone(String phone) throws ServiceException, ConnectionDBException {
        User user;
        try (UserDao userDao = daoFactory.createUserDao()) {
            user = userDao.getByPhone(phone).orElseThrow(() -> new ServiceException("User not found"));
            return addRolesForUser(user);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while getting user by phone: %s", phone), e);
            throw new ServiceException("Error while getting user by phone", e);
        }
    }

    public String getUserPage(Page<User> page) throws ServiceException, ConnectionDBException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return new PageService<UserManagementDTO>().jsonifyPage(formDTOManagementPage(userDao.getPage(page)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while getting user page: %s", page), e);
            throw new ServiceException("Error while getting user page", e);
        }
    }

    private Page<UserManagementDTO> formDTOManagementPage(Page<User> page) {
        return Page.<UserManagementDTO>builder()
                .setSorting(page.getSorting())
                .setSearch(page.getSearch())
                .setElementsCount(page.getElementsCount())
                .setLimit(page.getLimit())
                .setPageNumber(page.getPageNumber())
                .setData(page.getData().stream()
                        .map(DTOMapper::userToUserManagementDTO)
                        .toList())
                .createPage();
    }

    public void updateUserRoles(User user) throws ServiceException, ConnectionDBException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.update(user);
        } catch (SQLException | JDBCException e) {
            LOGGER.error(String.format("Error while updating user: %s", user), e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while updating user roles: %s", user), e);
            throw new ServiceException("Error while updating user roles", e);
        }
    }

    public void updateUserPassword(User user, String newPassword) throws ServiceException, ConnectionDBException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.update(user, newPassword);
        } catch (SQLException | JDBCException e) {
            LOGGER.error(String.format("Error while updating user: %s", user), e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while updating user password for user: %s", user), e);
            throw new ServiceException("Error while updating user password", e);
        }
    }

    public void deleteUser(long userId) throws ServiceException, ConnectionDBException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.delete(userId);
        } catch (SQLException | JDBCException e) {
            LOGGER.error(String.format("Error while deleting user with id: %d", userId), e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while deleting user with id: %d", userId), e);
            throw new ServiceException("Error while deleting user", e);
        }
    }

    public void disableUserById(long userId) throws ServiceException, ConnectionDBException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.disable(userId);
        } catch (SQLException | JDBCException e) {
            LOGGER.error(String.format("Error while disabling user with id: %d", userId), e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while disabling user with id: %d", userId), e);
            throw new ServiceException("Error while disabling user", e);
        }
    }

    public void enableUserById(long userId) throws ServiceException, ConnectionDBException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.enable(userId);
        } catch (SQLException | JDBCException e) {
            LOGGER.error(String.format("Error while enabling user with id: %d", userId), e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while enabling user with id: %d", userId), e);
            throw new ServiceException("Error while enabling user", e);
        }
    }

    public void updateUserData(User user) throws ServiceException, ConnectionDBException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.updateData(user);
        } catch (SQLException | JDBCException e) {
            LOGGER.error(String.format("Error while updating user: %s", user), e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while updating user data: %s", user), e);
            throw new ServiceException("Error while updating user data", e);
        }
    }

    public User collectUserFromRequest(HttpServletRequest request) {
        return User.builder()
                .setLogin(
                        Utility.getStringParameter(
                                request.getParameter(Constants.RequestAttributes.APP_LOGIN_ATTRIBUTE),
                                Constants.APP_STRING_DEFAULT_VALUE))
                .setFirstName(
                        Utility.getStringParameter(
                                request.getParameter(
                                        Constants.RequestAttributes.ACCOUNT_FIRST_NAME_ATTRIBUTE
                                ),
                                Constants.APP_STRING_DEFAULT_VALUE))
                .setLastName(
                        Utility.getStringParameter(
                                request.getParameter(
                                        Constants.RequestAttributes.ACCOUNT_LAST_NAME_ATTRIBUTE
                                ),
                                Constants.APP_STRING_DEFAULT_VALUE))
                .setEmail(
                        Utility.getStringParameter(
                                request.getParameter(
                                        Constants.RequestAttributes.ACCOUNT_EMAIL_ATTRIBUTE
                                ),
                                Constants.APP_STRING_DEFAULT_VALUE))
                .setPhone(
                        Utility.getStringParameter(
                                request.getParameter(
                                        Constants.RequestAttributes.ACCOUNT_PHONE_ATTRIBUTE
                                ),
                                Constants.APP_STRING_DEFAULT_VALUE))
                .setEnabled(true)
                .createUser();
    }
}

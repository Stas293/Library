package ua.org.training.library.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.RoleDao;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public UserService() {
    }

    public UserService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public long createUser(User user, String password) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.create(user, password);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while creating user: %s", user), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while creating user: %s", user), e);
            throw new ServiceException(e);
        }
        return -1;
    }

    public User getUserById(long id) throws ServiceException {
        User user;
        try (UserDao userDao = daoFactory.createUserDao()) {
            user = userDao.getById(id).orElseThrow(() -> new ServiceException("User not found"));
            try (RoleDao roleDao = daoFactory.createRoleDao()) {
                List<Role> roles = roleDao.getRolesByUserId(id);
                if (roles.isEmpty()) {
                    throw new ServiceException("User has no roles");
                }
                user.setRoles(roleDao.getRolesByUserId(id));
            }
            return user;
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while getting user by id: %d", id), e);
            throw new ServiceException(e);
        }
        return null;
    }

    public User getUserByLogin(String login) throws ServiceException {
        User user;
        try (UserDao userDao = daoFactory.createUserDao()) {
            user = userDao.getByLogin(login).orElseThrow(() -> new ServiceException("User not found"));
            return addRolesForUser(user);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while getting user by login: %s", login), e);
            throw new ServiceException(e);
        }
        return null;
    }

    public User getUserByEmail(String email) throws ServiceException {
        User user;
        try (UserDao userDao = daoFactory.createUserDao()) {
            user = userDao.getByEmail(email).orElseThrow(() -> new ServiceException("User not found"));
            return addRolesForUser(user);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while getting user by email: %s", email), e);
            throw new ServiceException(e);
        }
        return null;
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

    public User getUserByPhone(String phone) throws ServiceException {
        User user;
        try (UserDao userDao = daoFactory.createUserDao()) {
            user = userDao.getByPhone(phone).orElseThrow(() -> new ServiceException("User not found"));
            return addRolesForUser(user);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while getting user by phone: %s", phone), e);
            throw new ServiceException(e);
        }
        return null;
    }

    public String getUserPage(Page<User> page) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return new PageService<User>().jsonifyPage(userDao.getPage(page));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while getting user page: %s", page), e);
            throw new ServiceException(e);
        }
        return null;
    }

    public void updateUserRoles(User user) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.update(user);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while updating user: %s", user), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting RoleDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while updating user roles: %s", user), e);
            throw new ServiceException(e);
        }
    }

    public void updateUserPassword(User user, String newPassword) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.update(user, newPassword);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while updating user: %s", user), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while updating user password for user: %s", user), e);
            throw new ServiceException(e);
        }
    }

    public void deleteUser(long userId) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.delete(userId);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while deleting user with id: %d", userId), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while deleting user with id: %d", userId), e);
            throw new ServiceException(e);
        }
    }

    public void disableUserById(long userId) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.disable(userId);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while disabling user with id: %d", userId), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while disabling user with id: %d", userId), e);
            throw new ServiceException(e);
        }
    }

    public void disableUserByLogin(String login) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.disable(login);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while disabling user with login: %s, ", login), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while disabling user with login: %s, ", login), e);
            throw new ServiceException(e);
        }
    }

    public void enableUserById(long userId) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.enable(userId);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while enabling user with id: %d", userId), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while enabling user with id: %d", userId), e);
            throw new ServiceException(e);
        }
    }

    public void enableUserByLogin(String login) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.enable(login);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while enabling user with login %s", login), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while enabling user with login %s", login), e);
            throw new ServiceException(e);
        }
    }

    public void updateEmailForUser(User user, String email) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.updateEmail(email, user);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while updating email for user: %s", user), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while updating email for user: %s", user), e);
            throw new ServiceException(e);
        }
    }

    public void updatePhoneForUser(User user, String phone) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.updatePhone(phone, user);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while updating phone for user: %s", user), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while updating phone for user: %s", user), e);
            throw new ServiceException(e);
        }
    }

    public void updateFirstNameForUser(User user, String firstName) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.updateFirstName(firstName, user);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while updating first name for user: %s", user), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while updating first name for user: %s", user), e);
            throw new ServiceException(e);
        }
    }

    public void updateLastNameForUser(User user, String lastName) throws ServiceException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.updateLastName(lastName, user);
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while updating last name for user: %s", user), e);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting UserDao", e);
        } catch (DaoException e) {
            LOGGER.error(String.format("Error while updating last name for user: %s", user), e);
            throw new ServiceException(e);
        }
    }

    public User collectUserFromRequest(HttpServletRequest request) {
        User user = new User();
        user.setLogin(
                Utility.getStringParameter(
                        request.getParameter(Constants.RequestAttributes.APP_LOGIN_ATTRIBUTE),
                        Constants.APP_STRING_DEFAULT_VALUE));
        user.setFirstName(
                Utility.getStringParameter(
                        request.getParameter(
                                Constants.RequestAttributes.ACCOUNT_FIRST_NAME_ATTRIBUTE
                        ),
                        Constants.APP_STRING_DEFAULT_VALUE));
        user.setLastName(
                Utility.getStringParameter(
                        request.getParameter(
                                Constants.RequestAttributes.ACCOUNT_LAST_NAME_ATTRIBUTE
                        ),
                        Constants.APP_STRING_DEFAULT_VALUE));
        user.setEmail(
                Utility.getStringParameter(
                        request.getParameter(
                                Constants.RequestAttributes.ACCOUNT_EMAIL_ATTRIBUTE
                        ),
                        Constants.APP_STRING_DEFAULT_VALUE));
        user.setPhone(
                Utility.getStringParameter(
                        request.getParameter(
                                Constants.RequestAttributes.ACCOUNT_PHONE_ATTRIBUTE
                        ),
                        Constants.APP_STRING_DEFAULT_VALUE));
        user.setEnabled(true);
        return user;
    }
}

package ua.org.training.library.dao;

import ua.org.training.library.model.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao extends GenericDao<User> {
    long create(User entity, String password) throws SQLException;

    void update(User entity, String password) throws SQLException;

    Optional<User> getByLogin(String username);

    Optional<User> getByEmail(String email);

    Optional<User> getByPhone(String phone);

    Optional<User> getByOrderId(Long id);

    Optional<User> getByHistoryOrderId(Long id);

    void disable(Long id) throws SQLException;

    void disable(String login) throws SQLException;

    void enable(Long id) throws SQLException;

    void enable(String login) throws SQLException;

    void updateEmail(String email, User user) throws SQLException;

    void updatePhone(String phone, User user) throws SQLException;

    void updateFirstName(String firstName, User user) throws SQLException;

    void updateLastName(String lastName, User user) throws SQLException;
}

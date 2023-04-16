package ua.org.training.library.dao;


import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;

import java.sql.Connection;
import java.util.Collection;
import java.util.Optional;

public interface UserDao extends GenericDao<User> {
    User create(Connection connection,
                User entity, String password);

    void update(Connection connection,
                User entity, String password);

    Optional<User> getByLogin(Connection connection,
                              String username);

    Optional<User> getByEmail(Connection connection,
                              String email);

    Optional<User> getByPhone(Connection connection,
                              String phone);

    Optional<User> getByOrderId(Connection connection,
                                Long id);

    Optional<User> getByHistoryOrderId(Connection connection,
                                       Long id);

    void disable(Connection connection,
                 Long id);

    void enable(Connection connection,
                Long id);

    void updateRolesByUserId(Connection connection,
                             Long id,
                             Collection<Role> roles);
}

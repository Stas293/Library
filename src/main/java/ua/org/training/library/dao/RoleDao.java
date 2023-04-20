package ua.org.training.library.dao;


import ua.org.training.library.model.Role;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface RoleDao extends GenericDao<Role> {
    Optional<Role> getByCode(Connection connection,
                             String code);

    Optional<Role> getByName(Connection connection,
                             String name);

    List<Role> getRolesByUserId(Connection connection,
                                Long id);

    List<Role> getAllByCodes(Connection connection, List<String> roles);
}

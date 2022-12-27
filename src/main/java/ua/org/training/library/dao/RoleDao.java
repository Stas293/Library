package ua.org.training.library.dao;

import ua.org.training.library.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDao extends GenericDao<Role> {
    Optional<Role> getByCode(String code);

    Optional<Role> getByName(String name);

    List<Role> getRolesByUserId(Long id);

    List<Role> getRolesByUserLogin(String login);

    List<Role> getAllRoles();
}

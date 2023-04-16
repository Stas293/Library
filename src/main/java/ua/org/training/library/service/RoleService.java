package ua.org.training.library.service;


import ua.org.training.library.model.Role;

import java.util.Optional;

public interface RoleService extends GenericService<Long, Role> {
    Optional<Role> getByCode(String code);

    Optional<Role> getByName(String name);
}

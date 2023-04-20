package ua.org.training.library.service;


import ua.org.training.library.dto.RoleDto;
import ua.org.training.library.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> getByCode(String code);

    Optional<Role> getByName(String name);

    List<RoleDto> getAllRoles();

    String[] findAllCode();
}

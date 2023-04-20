package ua.org.training.library.repository;


import ua.org.training.library.model.Role;
import ua.org.training.library.repository.base.JRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JRepository<Role, Long> {
    Optional<Role> findByCode(String code);

    Optional<Role> findByName(String name);

    List<Role> findAllByCodes(List<String> roles);
}

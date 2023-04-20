package ua.org.training.library.repository;


import ua.org.training.library.model.User;
import ua.org.training.library.repository.base.JRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Optional;

public interface UserRepository extends JRepository<User, Long> {
    User save(User user, String password);
    Optional<User> getByLogin(String username);

    Optional<User> getByEmail(String email);

    Optional<User> getByPhone(String phone);

    void disable(User user);

    void enable(User user);

    void updateRolesForUser(User user);

    void updatePassword(User user, String password);

    String getPasswordForUser(User user);

    Page<User> searchUsers(Pageable pageable, String search);
}

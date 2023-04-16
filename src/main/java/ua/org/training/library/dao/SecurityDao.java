package ua.org.training.library.dao;

import java.sql.Connection;

public interface SecurityDao {
    String getPasswordByLogin(Connection connection,
                              String username);
}

package ua.org.training.library.dao;

public interface SecurityDao extends AutoCloseable {
    String getPasswordByLogin(String username);
}

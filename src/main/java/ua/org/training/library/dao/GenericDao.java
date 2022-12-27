package ua.org.training.library.dao;

import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.Optional;

public interface GenericDao<T> extends AutoCloseable {
    long create(T model) throws SQLException;

    Optional<T> getById(long id);

    Page<T> getPage(Page<T> page);

    void update(T entity) throws SQLException;

    void delete(long id) throws SQLException;

    void close();
}

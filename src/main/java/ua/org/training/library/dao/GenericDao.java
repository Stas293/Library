package ua.org.training.library.dao;


import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    T create(Connection connection, T model);

    List<T> create(Connection connection, List<T> models);

    Optional<T> getById(Connection connection, long id);

    List<T> getByIds(Connection connection, List<Long> ids);

    List<T> getAll(Connection connection);

    List<T> getAll(Connection connection, Sort sort);

    Page<T> getPage(Connection connection, Pageable page);

    void update(Connection connection, T entity);

    void delete(Connection connection, long id);

    long count(Connection conn);

    void deleteAll(Connection conn);

    void deleteAll(Connection conn, List<? extends Long> longs);

    void update(Connection conn, List<T> entities);
}

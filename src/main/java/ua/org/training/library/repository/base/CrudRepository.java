package ua.org.training.library.repository.base;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    T save(T entity);

    List<T> saveAll(List<T> entities);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    List<T> findAll();

    List<T> findAllById(List<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAllById(List<? extends Long> ids);

    void deleteAll(List<? extends T> entities);

    void deleteAll();
}

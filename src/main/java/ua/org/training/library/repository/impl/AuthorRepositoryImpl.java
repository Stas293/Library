package ua.org.training.library.repository.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.connections.TransactionManager;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.model.Author;
import ua.org.training.library.repository.AuthorRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorRepositoryImpl implements AuthorRepository {
    private final AuthorDao dao;
    private final TransactionManager transactionManager;

    @Override
    public Author save(Author entity) {
        log.info("Saving author: {}", entity);
        Connection conn = transactionManager.getConnection();
        if (entity.getId() == null) {
            log.info("Creating author: {}", entity);
            entity  = dao.create(conn, entity);
        } else {
            log.info("Updating author: {}", entity);
            dao.update(conn, entity);
        }
        return entity;
    }

    @Override
    public List<Author> saveAll(List<Author> entities) {
        log.info("Saving authors: {}", entities);
        Connection conn = transactionManager.getConnection();
        List<Author> authorsToSave = entities.stream()
                .filter(author -> author.getId() == null)
                .collect(Collectors.toList());
        List<Author> authorsToUpdate = entities.stream()
                .filter(author -> author.getId() != null)
                .toList();
        if (!authorsToSave.isEmpty()) {
            log.info("Creating authors: {}", authorsToSave);
            authorsToSave = dao.create(conn, authorsToSave);
        }
        if (!authorsToUpdate.isEmpty()) {
            log.info("Updating authors: {}", authorsToUpdate);
            dao.update(conn, authorsToUpdate);
        }
        authorsToSave.addAll(authorsToUpdate);
        return authorsToSave;
    }


    @Override
    public Optional<Author> findById(Long aLong) {
        log.info("Getting author by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        return dao.getById(conn, aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        log.info("Checking if author exists by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        return dao.getById(conn, aLong).isPresent();
    }

    @Override
    public List<Author> findAll() {
        log.info("Getting all authors");
        Connection conn = transactionManager.getConnection();
        return dao.getAll(conn);
    }

    @Override
    public List<Author> findAllById(List<Long> longs) {
        log.info("Getting authors by ids: {}", longs);
        Connection conn = transactionManager.getConnection();
        return dao.getByIds(conn, longs);
    }

    @Override
    public long count() {
        log.info("Counting authors");
        Connection conn = transactionManager.getConnection();
        return dao.count(conn);
    }

    @Override
    public void deleteById(Long aLong) {
        log.info("Deleting author by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        dao.delete(conn, aLong);
    }

    @Override
    public void delete(Author entity) {
        log.info("Deleting author: {}", entity);
        Connection conn = transactionManager.getConnection();
        dao.delete(conn, entity.getId());
    }

    @Override
    public void deleteAllById(List<? extends Long> longs) {
        log.info("Deleting authors by ids: {}", longs);
        Connection conn = transactionManager.getConnection();
        dao.deleteAll(conn, longs);
    }

    @Override
    public void deleteAll(List<? extends Author> entities) {
        log.info("Deleting authors: {}", entities);
        Connection conn = transactionManager.getConnection();
        List<Long> ids = entities.stream().map(Author::getId).toList();
        log.info("Deleting authors by ids: {}", ids);
        dao.deleteAll(conn, ids);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all authors");
        Connection conn = transactionManager.getConnection();
        dao.deleteAll(conn);
    }

    @Override
    public List<Author> findAll(Sort sort) {
        log.info("Getting all authors sorted by: {}", sort);
        Connection conn = transactionManager.getConnection();
        return dao.getAll(conn, sort);
    }

    @Override
    public Page<Author> findAll(Pageable pageable) {
        log.info("Getting all authors by page: {}", pageable);
        Connection conn = transactionManager.getConnection();
        return dao.getPage(conn, pageable);
    }
}

package ua.org.training.library.repository.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.connections.TransactionManager;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dao.KeywordDao;
import ua.org.training.library.model.Keyword;
import ua.org.training.library.repository.KeywordRepository;
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
public class KeywordRepositoryImpl implements KeywordRepository {
    private final TransactionManager transactionManager;
    private final KeywordDao keywordDao;

    @Override
    public Keyword save(Keyword entity) {
        log.info("Saving keyword: {}", entity);
        Connection conn = transactionManager.getConnection();
        if (entity.getId() == null) {
            log.info("Creating keyword: {}", entity);
            entity = keywordDao.create(conn, entity);
        } else {
            log.info("Updating keyword: {}", entity);
            keywordDao.update(conn, entity);
        }
        return entity;
    }

    @Override
    public List<Keyword> saveAll(List<Keyword> entities) {
        log.info("Saving keywords: {}", entities);
        Connection conn = transactionManager.getConnection();
        List<Keyword> keywordsToSave = entities.stream()
                .filter(keyword -> keyword.getId() == null)
                .collect(Collectors.toList());
        List<Keyword> keywordsToUpdate = entities.stream()
                .filter(keyword -> keyword.getId() != null)
                .toList();
        if (!keywordsToSave.isEmpty()) {
            log.info("Creating keywords: {}", keywordsToSave);
            keywordsToSave = keywordDao.create(conn, keywordsToSave);
        }
        if (!keywordsToUpdate.isEmpty()) {
            log.info("Updating keywords: {}", keywordsToUpdate);
            keywordDao.update(conn, keywordsToUpdate);
        }
        keywordsToSave.addAll(keywordsToUpdate);
        return keywordsToSave;
    }

    @Override
    public Optional<Keyword> findById(Long aLong) {
        log.info("Finding keyword by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        Keyword keyword = keywordDao.getById(conn, aLong).orElse(null);
        return Optional.ofNullable(keyword);
    }

    @Override
    public boolean existsById(Long aLong) {
        log.info("Checking if keyword exists by id: {}", aLong);
        return aLong != null && keywordDao.getById(transactionManager.getConnection(), aLong).isPresent();
    }

    @Override
    public List<Keyword> findAll() {
        log.info("Finding all keywords");
        Connection conn = transactionManager.getConnection();
        return keywordDao.getAll(conn);
    }

    @Override
    public List<Keyword> findAllById(List<Long> longs) {
        log.info("Finding keywords by ids: {}", longs);
        Connection conn = transactionManager.getConnection();
        return keywordDao.getByIds(conn, longs);
    }

    @Override
    public long count() {
        log.info("Counting keywords");
        Connection conn = transactionManager.getConnection();
        return keywordDao.count(conn);
    }

    @Override
    public void deleteById(Long aLong) {
        log.info("Deleting keyword by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        keywordDao.delete(conn, aLong);
    }

    @Override
    public void delete(Keyword entity) {
        log.info("Deleting keyword: {}", entity);
        Connection conn = transactionManager.getConnection();
        keywordDao.delete(conn, entity.getId());
    }

    @Override
    public void deleteAllById(List<? extends Long> ids) {
        log.info("Deleting keywords by ids: {}", ids);
        Connection conn = transactionManager.getConnection();
        keywordDao.deleteAll(conn, ids);
    }

    @Override
    public void deleteAll(List<? extends Keyword> entities) {
        log.info("Deleting keywords: {}", entities);
        Connection conn = transactionManager.getConnection();
        List<Long> ids = entities.stream()
                .map(Keyword::getId)
                .toList();
        keywordDao.deleteAll(conn, ids);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all keywords");
        Connection conn = transactionManager.getConnection();
        keywordDao.deleteAll(conn);
    }

    @Override
    public List<Keyword> findAll(Sort sort) {
        log.info("Finding all keywords by sort: {}", sort);
        Connection conn = transactionManager.getConnection();
        return keywordDao.getAll(conn, sort);
    }

    @Override
    public Page<Keyword> findAll(Pageable pageable) {
        log.info("Finding all keywords by pageable: {}", pageable);
        Connection conn = transactionManager.getConnection();
        return keywordDao.getPage(conn, pageable);
    }

    @Override
    public List<Keyword> getKeywordsByQuery(String query) {
        log.info("Finding keywords by query: {}", query);
        Connection conn = transactionManager.getConnection();
        return keywordDao.getKeywordsByQuery(conn, query);
    }

    @Override
    public Optional<Keyword> findByData(String keyword) {
        log.info("Finding keyword by data: {}", keyword);
        Connection conn = transactionManager.getConnection();
        return keywordDao.getByData(conn, keyword);
    }
}

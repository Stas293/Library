package ua.org.training.library.repository.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.connections.TransactionManager;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dao.HistoryOrderDao;
import ua.org.training.library.dao.StatusDao;
import ua.org.training.library.dao.StatusNameDao;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.HistoryOrderRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HistoryOrderRepositoryImpl implements HistoryOrderRepository {
    private final TransactionManager transactionManager;
    private final StatusDao statusDao;
    private final StatusNameDao statusNameDao;
    private final UserDao userDao;
    private final HistoryOrderDao historyOrderDao;

    @Override
    public Page<HistoryOrder> getPageByUser(Pageable page, User user, Locale locale) {
        log.info("Getting page of history orders by user: {}", user);
        Connection conn = transactionManager.getConnection();
        Page<HistoryOrder> historyOrders = historyOrderDao.getPageByUserId(conn, page, user.getId());
        log.info("History orders: {}", historyOrders);
        log.info("Getting status and user for history orders");
        for (HistoryOrder historyOrder : historyOrders.getContent()) {
            historyOrder.setStatus(statusDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
            historyOrder.getStatus().setNames(
                    List.of(
                            statusNameDao.getByStatusId(conn, historyOrder.getStatus().getId(), locale).orElseThrow()
                    )
            );
        }
        return historyOrders;
    }

    @Override
    public Page<HistoryOrder> getPageByUserAndSearch(Pageable page, User user, String search, Locale locale) {
        log.info("Getting page of history orders by user: {} and search: {}", user, search);
        Connection conn = transactionManager.getConnection();
        Page<HistoryOrder> historyOrders = historyOrderDao.getPageByUserIdAndSearch(conn, page, user.getId(), search);
        log.info("History orders: {}", historyOrders);
        log.info("Getting status and user for history orders");
        for (HistoryOrder historyOrder : historyOrders.getContent()) {
            historyOrder.setStatus(statusDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
            historyOrder.getStatus().setNames(
                    List.of(
                            statusNameDao.getByStatusId(conn, historyOrder.getStatus().getId(), locale).orElseThrow()
                    )
            );
        }
        return historyOrders;
    }

    @Override
    public HistoryOrder save(HistoryOrder entity) {
        log.info("Saving history order: {}", entity);
        Connection conn = transactionManager.getConnection();
        if (entity.getId() == null) {
            log.info("Creating history order: {}", entity);
            entity = historyOrderDao.create(conn, entity);
        } else {
            log.info("Updating history order: {}", entity);
            historyOrderDao.update(conn, entity);
        }
        log.info("History order saved: {}", entity);
        return entity;
    }

    @Override
    public List<HistoryOrder> saveAll(List<HistoryOrder> entities) {
        log.info("Saving history orders: {}", entities);
        Connection conn = transactionManager.getConnection();
        List<HistoryOrder> historyOrdersToSave = entities.stream()
                .filter(ho -> ho.getId() == null)
                .toList();
        List<HistoryOrder> historyOrdersToUpdate = entities.stream()
                .filter(ho -> ho.getId() != null)
                .toList();
        if (!historyOrdersToSave.isEmpty()) {
            log.info("Creating history orders: {}", historyOrdersToSave);
            historyOrdersToSave = historyOrderDao.create(conn, historyOrdersToSave);
        }
        if (!historyOrdersToUpdate.isEmpty()) {
            log.info("Updating history orders: {}", historyOrdersToUpdate);
            historyOrderDao.update(conn, historyOrdersToUpdate);
        }
        historyOrdersToSave.addAll(historyOrdersToUpdate);
        log.info("History orders saved: {}", historyOrdersToSave);
        for (HistoryOrder historyOrder : historyOrdersToSave) {
            historyOrder.setStatus(statusDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
            historyOrder.setUser(userDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
        }
        return historyOrdersToSave;
    }

    @Override
    public Optional<HistoryOrder> findById(Long aLong) {
        log.info("Getting history order by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        Optional<HistoryOrder> historyOrder = historyOrderDao.getById(conn, aLong);
        log.info("History order: {}", historyOrder);
        log.info("Getting status and user for history order");
        historyOrder.ifPresent(ho -> {
            ho.setStatus(statusDao.getByHistoryOrderId(conn, ho.getId()).orElse(null));
            ho.setUser(userDao.getByHistoryOrderId(conn, ho.getId()).orElse(null));
        });
        return historyOrder;
    }

    @Override
    public boolean existsById(Long aLong) {
        log.info("Checking if history order exists by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        return historyOrderDao.getById(conn, aLong).isPresent();
    }

    @Override
    public List<HistoryOrder> findAll() {
        log.info("Getting all history orders");
        Connection conn = transactionManager.getConnection();
        List<HistoryOrder> historyOrders = historyOrderDao.getAll(conn);
        log.info("History orders: {}", historyOrders);
        log.info("Getting status and user for history orders");
        for (HistoryOrder historyOrder : historyOrders) {
            historyOrder.setStatus(statusDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
            historyOrder.setUser(userDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
        }
        return historyOrders;
    }

    @Override
    public List<HistoryOrder> findAllById(List<Long> longs) {
        log.info("Getting history orders by ids: {}", longs);
        Connection conn = transactionManager.getConnection();
        List<HistoryOrder> historyOrders = historyOrderDao.getByIds(conn, longs);
        log.info("History orders: {}", historyOrders);
        log.info("Getting status and user for history orders");
        for (HistoryOrder historyOrder : historyOrders) {
            historyOrder.setStatus(statusDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
            historyOrder.setUser(userDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
        }
        return historyOrders;
    }

    @Override
    public long count() {
        log.info("Getting count of history orders");
        Connection conn = transactionManager.getConnection();
        return historyOrderDao.count(conn);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting history order by id: {}", id);
        Connection conn = transactionManager.getConnection();
        historyOrderDao.delete(conn, id);
    }

    @Override
    public void delete(HistoryOrder entity) {
        log.info("Deleting history order: {}", entity);
        Connection conn = transactionManager.getConnection();
        historyOrderDao.delete(conn, entity.getId());
    }

    @Override
    public void deleteAllById(List<? extends Long> ids) {
        log.info("Deleting history orders by ids: {}", ids);
        Connection conn = transactionManager.getConnection();
        historyOrderDao.deleteAll(conn, ids);
    }

    @Override
    public void deleteAll(List<? extends HistoryOrder> entities) {
        log.info("Deleting history orders: {}", entities);
        Connection conn = transactionManager.getConnection();
        List<Long> ids = entities.stream().map(HistoryOrder::getId).toList();
        historyOrderDao.deleteAll(conn, ids);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all history orders");
        Connection conn = transactionManager.getConnection();
        historyOrderDao.deleteAll(conn);
    }

    @Override
    public List<HistoryOrder> findAll(Sort sort) {
        log.info("Getting all history orders by sort: {}", sort);
        Connection conn = transactionManager.getConnection();
        List<HistoryOrder> historyOrders = historyOrderDao.getAll(conn, sort);
        log.info("History orders: {}", historyOrders);
        log.info("Getting status and user for history orders");
        for (HistoryOrder historyOrder : historyOrders) {
            historyOrder.setStatus(statusDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
            historyOrder.setUser(userDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
        }
        return historyOrders;
    }

    @Override
    public Page<HistoryOrder> findAll(Pageable pageable) {
        log.info("Getting all history orders by page: {}", pageable);
        Connection conn = transactionManager.getConnection();
        Page<HistoryOrder> historyOrders = historyOrderDao.getPage(conn, pageable);
        log.info("History orders: {}", historyOrders);
        log.info("Getting status and user for history orders");
        for (HistoryOrder historyOrder : historyOrders.getContent()) {
            historyOrder.setStatus(statusDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
            historyOrder.setUser(userDao.getByHistoryOrderId(conn, historyOrder.getId()).orElse(null));
        }
        return historyOrders;
    }
}

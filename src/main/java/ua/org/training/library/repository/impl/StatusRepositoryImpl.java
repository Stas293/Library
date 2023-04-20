package ua.org.training.library.repository.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.connections.TransactionManager;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dao.StatusDao;
import ua.org.training.library.dao.StatusNameDao;
import ua.org.training.library.model.Status;
import ua.org.training.library.model.StatusName;
import ua.org.training.library.repository.StatusRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatusRepositoryImpl implements StatusRepository {
    private final StatusDao statusDao;
    private final StatusNameDao statusNameDao;
    private final TransactionManager transactionManager;

    @Override
    public Optional<Status> findByCode(String code) {
        log.info("Getting status by code: {}", code);
        Optional<Status> status = statusDao.getByCode(transactionManager.getConnection(), code);
        status.ifPresent(value -> value.setNames(
                statusNameDao.getByStatusId(transactionManager.getConnection(), value.getId()))
        );
        return status;
    }

    @Override
    public List<Status> getNextStatusesForStatusById(Long id, Locale locale) {
        log.info("Getting next statuses for status by id: {}", id);
        List<Status> statuses = statusDao.getNextStatusesForStatusById(transactionManager.getConnection(), id);
        statuses.forEach(status -> status.setNames(
                        List.of(
                                statusNameDao.getByStatusId(transactionManager.getConnection(), status.getId(), locale).orElseThrow()
                        )
                )
        );
        return statuses;
    }

    @Override
    public Optional<Status> getStatusByOrderId(long id) {
        log.info("Getting statuses by order id: {}", id);
        return statusDao.getByOrderId(transactionManager.getConnection(), id);
    }

    @Override
    public Status save(Status entity) {
        log.info("Saving status: {}", entity);
        if (entity.getId() == null) {
            log.info("Creating status: {}", entity);
            Status savedStatus = statusDao.create(transactionManager.getConnection(), entity);
            List<StatusName> statusNames = entity.getNames();
            statusNames.forEach(statusName -> statusName.setStatus(savedStatus));
            statusNames = statusNameDao.create(transactionManager.getConnection(), statusNames);
            savedStatus.setNames(statusNames);
            return savedStatus;
        } else {
            log.info("Updating status: {}", entity);
            statusDao.update(transactionManager.getConnection(), entity);
            List<StatusName> statusNames = entity.getNames();
            statusNames.forEach(statusName -> statusName.setStatus(entity));
            statusNameDao.update(transactionManager.getConnection(), statusNames);
            return entity;
        }
    }

    @Override
    public List<Status> saveAll(List<Status> entities) {
        log.info("Saving statuses: {}", entities);
        List<Status> statusesToSave = entities.stream()
                .filter(status -> status.getId() == null)
                .toList();
        List<Status> statusesToUpdate = entities.stream()
                .filter(status -> status.getId() != null)
                .toList();
        if (!statusesToSave.isEmpty()) {
            log.info("Creating statuses: {}", statusesToSave);
            statusesToSave = statusDao.create(transactionManager.getConnection(), statusesToSave);
            statusesToSave.forEach(status -> {
                List<StatusName> statusNames = status.getNames();
                statusNames.forEach(statusName -> statusName.setStatus(status));
                statusNames = statusNameDao.create(transactionManager.getConnection(), statusNames);
                status.setNames(statusNames);
            });
        }
        if (!statusesToUpdate.isEmpty()) {
            log.info("Updating statuses: {}", statusesToUpdate);
            statusDao.update(transactionManager.getConnection(), statusesToUpdate);
            statusesToUpdate.forEach(status -> {
                List<StatusName> statusNames = status.getNames();
                statusNames.forEach(statusName -> statusName.setStatus(status));
                statusNameDao.update(transactionManager.getConnection(), statusNames);
            });
        }
        statusesToSave.addAll(statusesToUpdate);
        return statusesToSave;
    }

    @Override
    public Optional<Status> findById(Long aLong) {
        log.info("Getting status by id: {}", aLong);
        Optional<Status> status = statusDao.getById(transactionManager.getConnection(), aLong);
        status.ifPresent(value -> value.setNames(
                statusNameDao.getByStatusId(transactionManager.getConnection(), value.getId()))
        );
        return status;
    }

    @Override
    public boolean existsById(Long aLong) {
        log.info("Checking if status exists by id: {}", aLong);
        return statusDao.getById(transactionManager.getConnection(), aLong).isPresent();
    }

    @Override
    public List<Status> findAll() {
        log.info("Getting all statuses");
        List<Status> statuses = statusDao.getAll(transactionManager.getConnection());
        statuses.forEach(status -> status.setNames(
                statusNameDao.getByStatusId(transactionManager.getConnection(), status.getId()))
        );
        return statuses;
    }

    @Override
    public List<Status> findAllById(List<Long> longs) {
        log.info("Getting statuses by ids: {}", longs);
        List<Status> statuses = statusDao.getByIds(transactionManager.getConnection(), longs);
        statuses.forEach(status -> status.setNames(
                statusNameDao.getByStatusId(transactionManager.getConnection(), status.getId()))
        );
        return statuses;
    }

    @Override
    public long count() {
        log.info("Counting statuses");
        return statusDao.count(transactionManager.getConnection());
    }

    @Override
    public void deleteById(Long aLong) {
        log.info("Deleting status by id: {}", aLong);
        statusDao.delete(transactionManager.getConnection(), aLong);
    }

    @Override
    public void delete(Status entity) {
        log.info("Deleting status: {}", entity);
        statusDao.delete(transactionManager.getConnection(), entity.getId());
    }

    @Override
    public void deleteAllById(List<? extends Long> ids) {
        log.info("Deleting statuses by ids: {}", ids);
        statusDao.deleteAll(transactionManager.getConnection(), ids);
    }

    @Override
    public void deleteAll(List<? extends Status> entities) {
        log.info("Deleting statuses: {}", entities);
        List<Long> ids = entities.stream()
                .map(Status::getId)
                .toList();
        statusDao.deleteAll(transactionManager.getConnection(), ids);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all statuses");
        statusDao.deleteAll(transactionManager.getConnection());
    }

    @Override
    public List<Status> findAll(Sort sort) {
        log.info("Getting all statuses sorted by: {}", sort);
        List<Status> statuses = statusDao.getAll(transactionManager.getConnection(), sort);
        statuses.forEach(status -> status.setNames(
                statusNameDao.getByStatusId(transactionManager.getConnection(), status.getId()))
        );
        return statuses;
    }

    @Override
    public Page<Status> findAll(Pageable pageable) {
        log.info("Getting all statuses with pagination: {}", pageable);
        Page<Status> statusPage = statusDao.getPage(transactionManager.getConnection(), pageable);
        statusPage.getContent().forEach(status -> status.setNames(
                statusNameDao.getByStatusId(transactionManager.getConnection(), status.getId()))
        );
        return statusPage;
    }
}

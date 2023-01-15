package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.StatusDao;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Status;

import java.util.List;

public class StatusService {
    private static final Logger LOGGER = LogManager.getLogger(StatusService.class);
    private final DaoFactory daoFactory;

    public StatusService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public Status getByCode(String statusCode) throws ServiceException, ConnectionDBException {
        try (StatusDao statusDao = daoFactory.createStatusDao()) {
            return statusDao.getByCode(statusCode)
                    .orElseThrow(() -> new ServiceException("Status not found"));
        } catch (JDBCException e) {
            LOGGER.error("Cannot find status by code", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Cannot find status by code", e);
            throw new ServiceException("Cannot find status by code", e);
        }
    }

    public boolean hasNextStatus(Status status, Status newStatus) throws ServiceException, ConnectionDBException {
        try (StatusDao statusDao = daoFactory.createStatusDao()) {
            List<Status> nextStatuses = statusDao.getNextStatusesForStatusById(status.getId());
            for (Status nextStatus : nextStatuses)
                if (nextStatus.getCode().equals(newStatus.getCode())) return true;
            return false;
        } catch (JDBCException e) {
            LOGGER.error("Cannot find next statuses for status", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Cannot find status by code", e);
            throw new ServiceException("Cannot find status by code", e);
        }
    }
}

package ua.org.training.library.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private StatusDao statusDao;
    private StatusService statusService;

    @Test
    void getByCode() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createStatusDao()).thenReturn(statusDao);
        statusService = new StatusService(daoFactory);
        Status status = Status.builder()
                .setId(1L)
                .setName("ACTIVE")
                .setCode("ACTIVE")
                .createStatus();
        Mockito.when(statusDao.getByCode("ACTIVE")).thenReturn(Optional.ofNullable(status));
        assertEquals(status, statusService.getByCode("ACTIVE"));

        Mockito.when(statusDao.getByCode("ACTIVE")).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> statusService.getByCode("ACTIVE"));

        Mockito.when(statusDao.getByCode("ACTIVE")).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> statusService.getByCode("ACTIVE"));

        Mockito.when(daoFactory.createStatusDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> statusService.getByCode("ACTIVE"));
    }

    @Test
    void hasNextStatus() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createStatusDao()).thenReturn(statusDao);
        statusService = new StatusService(daoFactory);
        Status status = Status.builder()
                .setId(1L)
                .setName("ACTIVE")
                .setCode("ACTIVE")
                .createStatus();
        Status newStatus = Status.builder()
                .setId(2L)
                .setName("INACTIVE")
                .setCode("INACTIVE")
                .createStatus();
        Mockito.when(statusDao.getNextStatusesForStatusById(1L)).thenReturn(List.of());
        assertFalse(statusService.hasNextStatus(status, newStatus));

        Mockito.when(statusDao.getNextStatusesForStatusById(1L)).thenReturn(List.of(newStatus));
        assertTrue(statusService.hasNextStatus(status, newStatus));

        Mockito.when(statusDao.getNextStatusesForStatusById(1L)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> statusService.hasNextStatus(status, newStatus));

        Mockito.when(daoFactory.createStatusDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> statusService.hasNextStatus(status, newStatus));
    }
}
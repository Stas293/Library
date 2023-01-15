package ua.org.training.library.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.HistoryOrderDao;
import ua.org.training.library.dao.StatusDao;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.Status;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class HistoryOrderServiceTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private HistoryOrderDao historyOrderDao;
    @Mock
    private StatusDao statusDao;
    @Mock
    private UserDao userDao;
    private HistoryOrderService historyOrderService;

    @Test
    void createHistoryOrder() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createHistoryOrderDao()).thenReturn(historyOrderDao);
        Mockito.when(historyOrderDao.create(Mockito.any(HistoryOrder.class))).thenReturn(1L);
        historyOrderService = new HistoryOrderService(daoFactory);
        HistoryOrder historyOrder = HistoryOrder.builder()
                .setBookName("bookName")
                .setDateCreated(new Timestamp(1))
                .setDateExpire(new Timestamp(1))
                .setStatus(Status.builder().setId(1).createStatus())
                .setUser(User.builder().setId(1L).createUser())
                .createHistoryOrder();
        Assertions.assertEquals(1, historyOrderService.createHistoryOrder(historyOrder));

        Mockito.when(historyOrderDao.create(Mockito.any(HistoryOrder.class))).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> historyOrderService.createHistoryOrder(historyOrder));

        Mockito.when(historyOrderDao.create(Mockito.any(HistoryOrder.class))).thenThrow(SQLException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> historyOrderService.createHistoryOrder(historyOrder));

        Mockito.when(daoFactory.createHistoryOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> historyOrderService.createHistoryOrder(historyOrder));
    }

    @Test
    void getHistoryOrderById() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createHistoryOrderDao()).thenReturn(historyOrderDao);
        Mockito.when(historyOrderDao.getById(Mockito.anyLong())).thenReturn(
                Optional.ofNullable(HistoryOrder.builder().createHistoryOrder()));
        historyOrderService = new HistoryOrderService(daoFactory);
        Assertions.assertNotNull(historyOrderService.getHistoryOrderById(1L));

        Mockito.when(historyOrderDao.getById(Mockito.anyLong())).thenReturn(
                Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> historyOrderService.getHistoryOrderById(1L));

        Mockito.when(historyOrderDao.getById(Mockito.anyLong())).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> historyOrderService.getHistoryOrderById(1L));

        Mockito.when(daoFactory.createHistoryOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> historyOrderService.getHistoryOrderById(1L));
    }

    @Test
    void getHistoryOrderPage() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createHistoryOrderDao()).thenReturn(historyOrderDao);
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createStatusDao()).thenReturn(statusDao);
        Mockito.when(userDao.getByHistoryOrderId(Mockito.anyLong())).thenReturn(
                Optional.ofNullable(
                        User.builder()
                                .setId(1L)
                                .setLogin("login")
                                .setFirstName("firstName")
                                .setLastName("lastName")
                                .setEmail("email")
                                .setPhone("phone")
                                .setDateUpdated(new Timestamp(1))
                                .setDateCreated(new Timestamp(1))
                                .setEnabled(true)
                                .createUser()
                )
        );
        Mockito.when(statusDao.getByHistoryOrderId(Mockito.anyLong())).thenReturn(
                Optional.ofNullable(
                        Status.builder()
                                .setId(1)
                                .setName("name")
                                .setCode("DONE")
                                .setClosed(false)
                                .createStatus()
                )
        );
        Page<HistoryOrder> page = Page.<HistoryOrder>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .setSearch("")
                .createPage();
        Page<HistoryOrder> pageToReturn = Page.<HistoryOrder>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .setSearch("")
                .setElementsCount(1)
                .setData(
                        List.of(
                                HistoryOrder.builder()
                                        .setId(1L)
                                        .setBookName("bookName")
                                        .setDateCreated(new Timestamp(1))
                                        .setDateExpire(new Timestamp(1))
                                        .createHistoryOrder()
                        )
                )
                .createPage();
        Mockito.when(historyOrderDao.getPage(page)).thenReturn(pageToReturn);
        historyOrderService = new HistoryOrderService(daoFactory);
        Assertions.assertEquals("{" +
                "\"elementsCount\":1," +
                "\"limit\":5," +
                "\"content\":[" +
                "{\"id\":1," +
                "\"user\":{\"login\":\"login\",\"firstName\":\"firstName\",\"lastName\":\"lastName\"," +
                "\"email\":\"email\",\"phone\":\"phone\",\"fullName\":\"firstName lastName\"}," +
                "\"bookName\":\"bookName\",\"dateCreated\":\"1970-01-01\",\"dateExpire\":\"1970-01-01\"," +
                "\"status\":" +
                "{\"code\":\"DONE\",\"name\":\"name\",\"value\":\"The book is returned\",\"nextStatuses\":[]}}]}",
                historyOrderService.getHistoryOrderPage(Locale.ENGLISH, page));

        Mockito.when(userDao.getByHistoryOrderId(Mockito.anyLong())).thenThrow(DaoException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> historyOrderService.getHistoryOrderPage(Locale.ENGLISH, page));

        Mockito.when(historyOrderDao.getPage(page)).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> historyOrderService.getHistoryOrderPage(Locale.ENGLISH, page));

        Mockito.when(daoFactory.createHistoryOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> historyOrderService.getHistoryOrderPage(Locale.ENGLISH, page));
    }

    @Test
    void updateHistoryOrder() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createHistoryOrderDao()).thenReturn(historyOrderDao);
        HistoryOrder historyOrder = HistoryOrder.builder()
                .setId(1L)
                .setBookName("bookName")
                .setDateCreated(new Timestamp(1))
                .setDateExpire(new Timestamp(1))
                .setStatus(Status.builder().setId(1).createStatus())
                .setUser(User.builder().setId(1L).createUser())
                .createHistoryOrder();

        historyOrderService = new HistoryOrderService(daoFactory);
        Assertions.assertDoesNotThrow(() -> historyOrderService.updateHistoryOrder(historyOrder));

        Mockito.doThrow(DaoException.class).when(historyOrderDao).update(Mockito.any(HistoryOrder.class));
        Assertions.assertThrows(ServiceException.class, () -> historyOrderService.updateHistoryOrder(historyOrder));

        Mockito.doThrow(SQLException.class).when(historyOrderDao).update(Mockito.any(HistoryOrder.class));
        Assertions.assertThrows(ConnectionDBException.class, () -> historyOrderService.updateHistoryOrder(historyOrder));

        Mockito.when(daoFactory.createHistoryOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> historyOrderService.updateHistoryOrder(historyOrder));
    }

    @Test
    void deleteHistoryOrder() throws JDBCException, SQLException {
        Mockito.when(daoFactory.createHistoryOrderDao()).thenReturn(historyOrderDao);
        historyOrderService = new HistoryOrderService(daoFactory);
        Assertions.assertDoesNotThrow(() -> historyOrderService.deleteHistoryOrder(1L));

        Mockito.doThrow(DaoException.class).when(historyOrderDao).delete(Mockito.anyLong());
        Assertions.assertThrows(ServiceException.class, () -> historyOrderService.deleteHistoryOrder(1L));

        Mockito.doThrow(SQLException.class).when(historyOrderDao).delete(Mockito.anyLong());
        Assertions.assertThrows(ConnectionDBException.class, () -> historyOrderService.deleteHistoryOrder(1L));

        Mockito.when(daoFactory.createHistoryOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> historyOrderService.deleteHistoryOrder(1L));
    }

    @Test
    void getHistoryOrderPageByUserId() throws JDBCException, ServiceException, ConnectionDBException {
        Mockito.when(daoFactory.createHistoryOrderDao()).thenReturn(historyOrderDao);
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createStatusDao()).thenReturn(statusDao);
        Mockito.when(userDao.getByHistoryOrderId(Mockito.anyLong())).thenReturn(
                Optional.ofNullable(
                        User.builder()
                                .setId(1L)
                                .setLogin("login")
                                .setFirstName("firstName")
                                .setLastName("lastName")
                                .setEmail("email")
                                .setPhone("phone")
                                .setDateUpdated(new Timestamp(1))
                                .setDateCreated(new Timestamp(1))
                                .setEnabled(true)
                                .createUser()
                )
        );
        Mockito.when(statusDao.getByHistoryOrderId(Mockito.anyLong())).thenReturn(
                Optional.ofNullable(
                        Status.builder()
                                .setId(1)
                                .setName("name")
                                .setCode("DONE")
                                .setClosed(false)
                                .createStatus()
                )
        );
        Page<HistoryOrder> page = Page.<HistoryOrder>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .setSearch("")
                .createPage();
        Page<HistoryOrder> pageToReturn = Page.<HistoryOrder>builder()
                .setLimit(5)
                .setPageNumber(0)
                .setSorting("ASC")
                .setSearch("")
                .setElementsCount(1)
                .setData(
                        List.of(
                                HistoryOrder.builder()
                                        .setId(1L)
                                        .setBookName("bookName")
                                        .setDateCreated(new Timestamp(1))
                                        .setDateExpire(new Timestamp(1))
                                        .createHistoryOrder()
                        )
                )
                .createPage();
        Mockito.when(historyOrderDao.getPageByUserId(page, 1L)).thenReturn(pageToReturn);
        historyOrderService = new HistoryOrderService(daoFactory);
        Assertions.assertEquals("{" +
                        "\"elementsCount\":1," +
                        "\"limit\":5," +
                        "\"content\":[" +
                        "{\"id\":1," +
                        "\"user\":{\"login\":\"login\",\"firstName\":\"firstName\",\"lastName\":\"lastName\"," +
                        "\"email\":\"email\",\"phone\":\"phone\",\"fullName\":\"firstName lastName\"}," +
                        "\"bookName\":\"bookName\",\"dateCreated\":\"1970-01-01\",\"dateExpire\":\"1970-01-01\"," +
                        "\"status\":" +
                        "{\"code\":\"DONE\",\"name\":\"name\",\"value\":\"The book is returned\",\"nextStatuses\":[]}}]}",
                historyOrderService.getHistoryOrderPageByUserId(Locale.ENGLISH, page, 1L));

        Mockito.when(historyOrderDao.getPageByUserId(page, 1L)).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> historyOrderService.getHistoryOrderPageByUserId(Locale.ENGLISH, page, 1L));

        Mockito.when(daoFactory.createHistoryOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> historyOrderService.getHistoryOrderPageByUserId(Locale.ENGLISH, page, 1L));
    }
}
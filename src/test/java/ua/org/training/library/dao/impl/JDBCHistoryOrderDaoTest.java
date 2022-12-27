package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.HistoryOrderDao;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JDBCHistoryOrderDaoTest {
    HistoryOrderDao historyOrderDao;
    @BeforeEach
    void setUp() {
        try {
            historyOrderDao = DaoFactory.getInstance().createHistoryOrderDao();
        } catch (JDBCException e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() throws SQLException, JDBCException {
        HistoryOrder historyOrder = HistoryOrder.builder()
                .setBookName("Test")
                .setUser(User.builder().setId(1L).createUser())
                .setDateCreated(new Date())
                .setDateExpire(new Date(new Date().getTime() + 24 * 60 * 60 * 1000))
                .createHistoryOrder();
        long id = historyOrderDao.create(historyOrder);
        setUp();
        assertEquals(id, historyOrderDao.getById(id).get().getId());
    }

    @Test
    void getById() {
        Date date = new Date(122, Calendar.DECEMBER, 4, 18,28,12);
        HistoryOrder historyOrder = HistoryOrder.builder()
                .setId(1L)
                .setBookName("Test")
                .setDateCreated(date)
                .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                .createHistoryOrder();
        assertEquals(Optional.of(historyOrder), historyOrderDao.getById(1L));
    }

    @Test
    void getPage() {
        Date date = new Date(122, Calendar.DECEMBER, 4, 18,28,12);
        HistoryOrder historyOrder = HistoryOrder.builder()
                .setId(1L)
                .setBookName("Test")
                .setDateCreated(date)
                .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                .createHistoryOrder();
        assertEquals(Page.<HistoryOrder>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(historyOrder))
                .createPage(), historyOrderDao.getPage(
                Page.<HistoryOrder>builder()
                        .setPageNumber(0)
                        .setLimit(10)
                        .setSorting("ASC")
                        .createPage()));

    }

    @Test
    void update() throws SQLException, JDBCException {
        Date date = new Date(122, Calendar.DECEMBER, 4, 18,28,12);
        HistoryOrder historyOrder = HistoryOrder.builder()
                .setId(1L)
                .setBookName("Test")
                .setDateCreated(date)
                .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                .setUser(DaoFactory.getInstance().createUserDao().getByLogin("Test").get())
                .createHistoryOrder();
        historyOrderDao.update(historyOrder);
        setUp();
        historyOrder.setUser(null);
        assertEquals(historyOrder, historyOrderDao.getById(1L).get());
    }

    @Test
    void delete() throws SQLException, JDBCException {
        historyOrderDao.delete(1L);
        setUp();
        assertEquals(Optional.empty(), historyOrderDao.getById(1L));
    }

    @Test
    void getPageByUserId() {
        Date date = new Date(122, Calendar.DECEMBER, 4, 18,28,12);
        HistoryOrder historyOrder = HistoryOrder.builder()
                .setId(1L)
                .setBookName("Test")
                .setDateCreated(date)
                .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                .createHistoryOrder();
        assertEquals(Page.<HistoryOrder>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(historyOrder))
                .createPage(), historyOrderDao.getPageByUserId(
                Page.<HistoryOrder>builder()
                        .setPageNumber(0)
                        .setLimit(10)
                        .setSorting("ASC")
                        .createPage(), 1L));
    }
}
package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.OrderDao;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.Order;
import ua.org.training.library.model.Place;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JDBCOrderDaoTest {
    OrderDao orderDao;
    @BeforeEach
    void setUp() {
        try {
            orderDao = DaoFactory.getInstance().createOrderDao();
        } catch (JDBCException e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() throws SQLException {
        Order order = Order.builder()
                .setBook(Book.builder().setId(1L).createBook())
                .setPlace(new Place(1L, "On a subscription"))
                .setUser(User.builder().setId(1L).setLogin("Test").createUser())
                .setDateCreated(new Date())
                .setDateExpire(new Date(new Date().getTime() + 24 * 60 * 60 * 1000))
                .createOrder();
        long id = orderDao.create(order);
        setUp();
        assertEquals(id, orderDao.getById(id).get().getId());
    }

    @Test
    void getById() {
        Date date = new Date(122, Calendar.DECEMBER, 4, 17, 48, 52);
        Order order = Order.builder()
                .setId(1L)
                .setDateCreated(date)
                .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                .createOrder();
        assertEquals(Optional.of(order), orderDao.getById(1L));
    }

    @Test
    void getPage() {
        Date date = new Date(122, Calendar.DECEMBER, 4, 17, 48, 52);
        assertEquals(Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(Order.builder()
                        .setId(1L)
                        .setDateCreated(date)
                        .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                        .createOrder()))
                .createPage(), orderDao.getPage(Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .createPage()));
    }

    @Test
    void delete() throws SQLException {
        orderDao.delete(1L);
        setUp();
        assertEquals(Optional.empty(), orderDao.getById(1L));
    }

    @Test
    void getPageByUserId() {
        Date date = new Date(122, Calendar.DECEMBER, 4, 17, 48, 52);
        assertEquals(Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(Order.builder()
                        .setId(1L)
                        .setDateCreated(date)
                        .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                        .createOrder()))
                .createPage(), orderDao.getPageByUserId(Page.<Order>builder()
                                .setPageNumber(0)
                                .setLimit(10)
                                .setSorting("ASC")
                                .createPage(), 1L));
    }

    @Test
    void getPageByPlaceId() {
        Date date = new Date(122, Calendar.DECEMBER, 4, 17, 48, 52);
        assertEquals(Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(Order.builder()
                        .setId(1L)
                        .setDateCreated(date)
                        .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                        .createOrder()))
                .createPage(), orderDao.getPageByPlaceId(Page.<Order>builder()
                        .setPageNumber(0)
                        .setLimit(10)
                        .setSorting("ASC")
                        .createPage(), 1L));
    }

    @Test
    void getPageByUserIdAndPlaceId() {
        Date date = new Date(122, Calendar.DECEMBER, 4, 17, 48, 52);
        assertEquals(Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(Order.builder()
                        .setId(1L)
                        .setDateCreated(date)
                        .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                        .createOrder()))
                .createPage(), orderDao.getPageByUserIdAndPlaceId(Page.<Order>builder()
                        .setPageNumber(0)
                        .setLimit(10)
                        .setSorting("ASC")
                        .createPage(), 1L, 1L));
    }

    @Test
    void getPageByPlaceName() {
        Date date = new Date(122, Calendar.DECEMBER, 4, 17, 48, 52);
        assertEquals(Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(Order.builder()
                        .setId(1L)
                        .setDateCreated(date)
                        .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                        .createOrder()))
                .createPage(), orderDao.getPageByPlaceName(Page.<Order>builder()
                        .setPageNumber(0)
                        .setLimit(10)
                        .setSorting("ASC")
                        .createPage(), "On a subscription"));
    }

    @Test
    void getPageByBookId() {
        Date date = new Date(122, Calendar.DECEMBER, 4, 17, 48, 52);
        assertEquals(Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(Order.builder()
                        .setId(1L)
                        .setDateCreated(date)
                        .setDateExpire(new Date(date.getTime() + 24 * 60 * 60 * 1000))
                        .createOrder()))
                .createPage(), orderDao.getPageByBookId(Page.<Order>builder()
                        .setPageNumber(0)
                        .setLimit(10)
                        .setSorting("ASC")
                        .createPage(), 1L));
    }
}
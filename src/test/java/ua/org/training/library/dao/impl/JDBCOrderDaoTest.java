package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.OrderDao;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.model.*;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.page.Page;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JDBCOrderDaoTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private CallableStatement callableStatement;
    @Mock
    private ResultSet resultSet;
    private OrderDao orderDao;

    @Test
    void create() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(1)).thenReturn(1L);
        PreparedStatement preparedStatement1 = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement1);
        ResultSet resultSet1 = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        Mockito.when(resultSet1.next()).thenReturn(true);
        Mockito.when(resultSet1.getInt("book_count")).thenReturn(1);
        orderDao = new JDBCOrderDao(connection);
        Order order = Order.builder()
                .setUser(User.builder().setId(1L).createUser())
                .setBook(Book.builder().setId(1L).createBook())
                .setPlace(Place.builder().setId(1L).createPlace())
                .setStatus(Status.builder().setId(1L).createStatus())
                .setDateCreated(new Date(1))
                .createOrder();

        assertEquals(1L, orderDao.create(order));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString(), Mockito.anyInt());
        Mockito.verify(preparedStatement, Mockito.times(4)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(preparedStatement, Mockito.times(1)).getGeneratedKeys();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong(1);
        Mockito.verify(connection, Mockito.times(2)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement1, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement1, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet1, Mockito.times(1)).next();
        Mockito.verify(resultSet1, Mockito.times(1)).getInt("book_count");
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.when(resultSet1.getInt("book_count")).thenReturn(0);
        assertEquals(Constants.APP_DEFAULT_ID, orderDao.create(order));

        Mockito.doThrow(SQLException.class).when(resultSet1).getInt("book_count");
        assertThrows(DaoException.class, () -> orderDao.create(order));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> orderDao.create(order));
    }

    @Test
    void getById() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getTimestamp("date_created")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getTimestamp("date_expire")).thenReturn(new Timestamp(1));

        orderDao = new JDBCOrderDao(connection);
        Optional<Order> expected = Optional.of(Order.builder()
                .setId(1L)
                .setDateCreated(new Date(1))
                .setDateExpire(new Date(1))
                .createOrder());
        assertEquals(expected, orderDao.getById(1L));

        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(1)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("date_created");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("date_expire");

        Mockito.when(resultSet.next()).thenReturn(false);
        assertEquals(Optional.empty(), orderDao.getById(1L));

        Mockito.doThrow(SQLException.class).when(resultSet).next();
        assertThrows(DaoException.class, () -> orderDao.getById(1L));

        Mockito.doThrow(SQLException.class).when(connection).prepareStatement(Mockito.anyString());
        assertThrows(DaoException.class, () -> orderDao.getById(1L));
    }

    @Test
    void getPage() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Field GET_ORDERS_PAGE = JDBCOrderDao.class.getDeclaredField("GET_ORDERS_PAGE");
        GET_ORDERS_PAGE.setAccessible(true);
        Mockito.when(connection.prepareCall(GET_ORDERS_PAGE.get("String").toString())).thenReturn(callableStatement);
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getTimestamp("date_created")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getTimestamp("date_expire")).thenReturn(new Timestamp(1));
        Mockito.when(callableStatement.getLong(5)).thenReturn(1L);

        orderDao = new JDBCOrderDao(connection);
        Page<Order> page = Page.<Order>builder()
                .setPageNumber(0)
                .setSearch("")
                .setSorting("ASC")
                .setLimit(5)
                .createPage();
        Page<Order> expected = Page.<Order>builder()
                .setPageNumber(0)
                .setSearch("")
                .setSorting("ASC")
                .setLimit(5)
                .setElementsCount(1)
                .setData(Collections.singletonList(Order.builder()
                        .setId(1L)
                        .setDateCreated(new Timestamp(1))
                        .setDateExpire(new Timestamp(1))
                        .createOrder()))
                .createPage();
        assertEquals(expected, orderDao.getPage(page));

        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(2)).next();
        Mockito.verify(resultSet, Mockito.times(1)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("date_created");
        Mockito.verify(resultSet, Mockito.times(1)).getTimestamp("date_expire");

        Mockito.doThrow(SQLException.class).when(resultSet).next();
        assertThrows(DaoException.class, () -> orderDao.getPage(page));
    }

    @Test
    void update() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        orderDao = new JDBCOrderDao(connection);
        Order order = Order.builder()
                .setId(1L)
                .setUser(User.builder().setId(1L).createUser())
                .setBook(Book.builder().setId(1L).createBook())
                .setPlace(Place.builder().setId(1L).createPlace())
                .setStatus(Status.builder().setId(1L).createStatus())
                .setDateCreated(new Date(1))
                .setDateExpire(new Date(1))
                .createOrder();
        assertDoesNotThrow(() -> orderDao.update(order));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(2)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(DaoException.class, () -> orderDao.update(order));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> orderDao.update(order));
    }

    @Test
    void delete() throws SQLException {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Order order = Order.builder()
                .setId(1L)
                .setUser(User.builder().setId(1L).createUser())
                .setBook(Book.builder().setId(1L).createBook())
                .setPlace(Place.builder().setId(1L).createPlace())
                .setStatus(Status.builder().setId(1L).createStatus())
                .setDateCreated(new Date(1))
                .setDateExpire(new Date(1))
                .createOrder();

        orderDao = new JDBCOrderDao(connection);
        assertDoesNotThrow(() -> orderDao.delete(order.getId()));

        Mockito.verify(connection, Mockito.times(2)).setAutoCommit(Mockito.anyBoolean());
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
        Mockito.verify(preparedStatement, Mockito.times(1)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(preparedStatement, Mockito.times(1)).executeUpdate();
        Mockito.verify(connection, Mockito.times(1)).commit();

        Mockito.doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(DaoException.class, () -> orderDao.delete(order.getId()));

        Mockito.doThrow(SQLException.class).when(connection).setAutoCommit(Mockito.anyBoolean());
        assertThrows(SQLException.class, () -> orderDao.delete(order.getId()));
    }

    @Test
    void close() throws SQLException {
        Mockito.doNothing().when(connection).close();
        orderDao = new JDBCOrderDao(connection);
        assertDoesNotThrow(() -> orderDao.close());
        Mockito.verify(connection, Mockito.times(1)).close();

        Mockito.doThrow(SQLException.class).when(connection).close();
        assertThrows(DaoException.class, () -> orderDao.close());
    }

    @Test
    void getPageByBookId() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getTimestamp("date_created")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getTimestamp("date_expire")).thenReturn(new Timestamp(1));
        Mockito.when(callableStatement.getLong(6)).thenReturn(2L);

        orderDao = new JDBCOrderDao(connection);
        Page<Order> expected = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(2)
                .setData(List.of(
                        Order.builder()
                        .setId(1L)
                        .setDateCreated(new Timestamp(1))
                        .setDateExpire(new Timestamp(1))
                        .createOrder(),
                        Order.builder()
                                .setId(1L)
                                .setDateCreated(new Timestamp(1))
                                .setDateExpire(new Timestamp(1))
                                .createOrder()
                        )
                )
                .createPage();
        Page<Order> page = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .createPage();
        assertEquals(expected, orderDao.getPageByBookId(page, 1L));

        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(2)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("date_created");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("date_expire");

        Mockito.doThrow(SQLException.class).when(callableStatement).executeQuery();
        assertThrows(DaoException.class, () -> orderDao.getPageByBookId(page, 1L));

        Mockito.doThrow(SQLException.class).when(connection).prepareCall(Mockito.anyString());
        assertThrows(DaoException.class, () -> orderDao.getPageByBookId(page, 1L));
    }

    @Test
    void getPageByStatusAndUserId() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getTimestamp("date_created")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getTimestamp("date_expire")).thenReturn(new Timestamp(1));
        Mockito.when(callableStatement.getLong(7)).thenReturn(2L);

        orderDao = new JDBCOrderDao(connection);
        Page<Order> expected = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(2)
                .setData(List.of(
                        Order.builder()
                        .setId(1L)
                        .setDateCreated(new Timestamp(1))
                        .setDateExpire(new Timestamp(1))
                        .createOrder(),
                        Order.builder()
                                .setId(1L)
                                .setDateCreated(new Timestamp(1))
                                .setDateExpire(new Timestamp(1))
                                .createOrder()
                        )
                )
                .createPage();
        Page<Order> page = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .createPage();
        assertEquals(expected, orderDao.getPageByStatusAndUserId(page, 1L, 1L));

        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(4)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(2)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("date_created");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("date_expire");

        Mockito.doThrow(SQLException.class).when(callableStatement).executeQuery();
        assertThrows(DaoException.class, () -> orderDao.getPageByStatusAndUserId(page, 1L, 1L));

        Mockito.doThrow(SQLException.class).when(connection).prepareCall(Mockito.anyString());
        assertThrows(DaoException.class, () -> orderDao.getPageByStatusAndUserId(page, 1L, 1L));
    }

    @Test
    void getPageByStatusId() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getTimestamp("date_created")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getTimestamp("date_expire")).thenReturn(new Timestamp(1));
        Mockito.when(callableStatement.getLong(7)).thenReturn(2L);

        orderDao = new JDBCOrderDao(connection);
        Page<Order> expected = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(2)
                .setData(List.of(
                        Order.builder()
                                .setId(1L)
                                .setDateCreated(new Timestamp(1))
                                .setDateExpire(new Timestamp(1))
                                .createOrder(),
                        Order.builder()
                                .setId(1L)
                                .setDateCreated(new Timestamp(1))
                                .setDateExpire(new Timestamp(1))
                                .createOrder()
                        )
                )
                .createPage();
        Page<Order> page = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .createPage();
        assertEquals(expected, orderDao.getPageByStatusId(page, 1L, "book_name"));

        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(2)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("date_created");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("date_expire");
        Mockito.verify(callableStatement, Mockito.times(1)).getLong(7);

        Mockito.doThrow(SQLException.class).when(callableStatement).executeQuery();
        assertThrows(DaoException.class, () -> orderDao.getPageByStatusId(page, 1L, "book_name"));

        Mockito.doThrow(SQLException.class).when(connection).prepareCall(Mockito.anyString());
        assertThrows(DaoException.class, () -> orderDao.getPageByStatusId(page, 1L, "book_name"));
    }

    @Test
    void getPageByStatusIdAndPlaceId() throws SQLException {
        Mockito.when(connection.prepareCall(Mockito.anyString())).thenReturn(callableStatement);
        Mockito.when(callableStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getLong("id")).thenReturn(1L);
        Mockito.when(resultSet.getTimestamp("date_created")).thenReturn(new Timestamp(1));
        Mockito.when(resultSet.getTimestamp("date_expire")).thenReturn(new Timestamp(1));
        Mockito.when(callableStatement.getLong(8)).thenReturn(2L);

        orderDao = new JDBCOrderDao(connection);
        Page<Order> expected = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(2)
                .setData(List.of(
                        Order.builder()
                                .setId(1L)
                                .setDateCreated(new Timestamp(1))
                                .setDateExpire(new Timestamp(1))
                                .createOrder(),
                        Order.builder()
                                .setId(1L)
                                .setDateCreated(new Timestamp(1))
                                .setDateExpire(new Timestamp(1))
                                .createOrder()
                        )
                )
                .createPage();
        Page<Order> page = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSorting("ASC")
                .setSearch("")
                .createPage();
        assertEquals(expected, orderDao.getPageByStatusIdAndPlaceId(page, 1L, 1L, "book_name"));

        Mockito.verify(connection, Mockito.times(1)).prepareCall(Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(3)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(callableStatement, Mockito.times(4)).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.verify(callableStatement, Mockito.times(1)).executeQuery();
        Mockito.verify(resultSet, Mockito.times(3)).next();
        Mockito.verify(resultSet, Mockito.times(2)).getLong("id");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("date_created");
        Mockito.verify(resultSet, Mockito.times(2)).getTimestamp("date_expire");

        Mockito.doThrow(SQLException.class).when(callableStatement).executeQuery();
        assertThrows(DaoException.class, () -> orderDao.getPageByStatusIdAndPlaceId(page, 1L, 1L, "book_name"));

        Mockito.doThrow(SQLException.class).when(connection).prepareCall(Mockito.anyString());
        assertThrows(DaoException.class, () -> orderDao.getPageByStatusIdAndPlaceId(page, 1L, 1L, "book_name"));
    }
}
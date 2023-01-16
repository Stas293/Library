package ua.org.training.library.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.*;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.*;
import ua.org.training.library.utility.page.Page;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private OrderDao orderDao;
    @Mock
    private StatusDao statusDao;
    @Mock
    private UserDao userDao;
    @Mock
    private BookDao bookDao;
    @Mock
    private PlaceDao placeDao;
    @Mock
    private AuthorDao authorDao;
    private OrderService orderService;

    @Test
    void createOrder() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createOrderDao()).thenReturn(orderDao);
        Order order = Order.builder()
                .setDateCreated(Date.valueOf("2020-01-01"))
                .setDateExpire(Date.valueOf("2020-01-01"))
                .setBook(Book.builder()
                        .setId(1L)
                        .createBook()
                )
                .setUser(User.builder()
                        .setId(1L)
                        .createUser()
                )
                .setPlace(Place.builder()
                        .setId(1L)
                        .createPlace()
                )
                .setStatus(
                        Status.builder()
                                .setId(1L)
                                .createStatus()
                )
                .createOrder();
        Mockito.when(orderDao.create(order)).thenReturn(1L);
        orderService = new OrderService(daoFactory);
        Assertions.assertEquals(1L, orderService.createOrder(order));

        Mockito.when(orderDao.create(order)).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> orderService.createOrder(order));

        Mockito.when(daoFactory.createOrderDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> orderService.createOrder(order));
    }

    @Test
    void getOrderById() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createOrderDao()).thenReturn(orderDao);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        Mockito.when(daoFactory.createStatusDao()).thenReturn(statusDao);
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Order order = Order.builder()
                .setId(1L)
                .setDateCreated(Date.valueOf("2020-01-01"))
                .setDateExpire(Date.valueOf("2020-01-01"))
                .createOrder();
        Order order1 = Order.builder()
                .setId(1L)
                .setDateCreated(Date.valueOf("2020-01-01"))
                .setDateExpire(Date.valueOf("2020-01-01"))
                .setBook(Book.builder()
                        .setId(1L)
                        .setAuthors(
                                List.of(
                                        Author.builder()
                                                .setId(1L)
                                                .createAuthor()
                                )
                        )
                        .createBook()
                )
                .setUser(User.builder()
                        .setId(1L)
                        .createUser()
                )
                .setPlace(Place.builder()
                        .setId(1L)
                        .createPlace()
                )
                .setStatus(
                        Status.builder()
                                .setId(1L)
                                .createStatus()
                )
                .createOrder();
        Mockito.when(bookDao.getBookByOrderId(1L)).thenReturn(Optional.of(Book.builder()
                .setId(1L)
                .createBook()));
        Mockito.when(placeDao.getByOrderId(1L)).thenReturn(Optional.of(Place.builder()
                .setId(1L)
                .createPlace()));
        Mockito.when(statusDao.getByOrderId(1L)).thenReturn(Optional.of(Status.builder()
                .setId(1L)
                .createStatus()));
        Mockito.when(userDao.getByOrderId(1L)).thenReturn(Optional.of(User.builder()
                .setId(1L)
                .createUser()));
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Mockito.when(authorDao.getAuthorsByBookId(1L)).thenReturn(List.of(
                        Author.builder()
                                .setId(1L)
                                .createAuthor()
                )
        );
        Mockito.when(orderDao.getById(1L)).thenReturn(Optional.ofNullable(order));
        orderService = new OrderService(daoFactory);
        Assertions.assertEquals(order, orderService.getOrderById(1L));

        Mockito.when(orderDao.getById(1L)).thenReturn(Optional.ofNullable(order));
        Mockito.when(bookDao.getBookByOrderId(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> orderService.getOrderById(1L));

        Mockito.when(orderDao.getById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> orderService.getOrderById(1L));

        Mockito.when(daoFactory.createOrderDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void getOrderPage() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createOrderDao()).thenReturn(orderDao);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        Mockito.when(daoFactory.createStatusDao()).thenReturn(statusDao);
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Order order = Order.builder()
                .setId(1L)
                .setDateCreated(new java.util.Date(1))
                .setDateExpire(new java.util.Date(1))
                .createOrder();
        Mockito.when(bookDao.getBookByOrderId(1L))
                .thenReturn(Optional.of(
                                Book.builder()
                                        .setId(1L)
                                        .setPublicationDate(new java.util.Date(1))
                                        .createBook()
                        )
                );
        Mockito.when(placeDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                Place.builder()
                                        .setId(1L)
                                        .setName("To the reading room")
                                        .createPlace()
                        )
                );
        Mockito.when(statusDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                Status.builder()
                                        .setId(1L)
                                        .setCode("ACCEPT")
                                        .createStatus()
                        )
                );
        Mockito.when(userDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                User.builder()
                                        .setId(1L)
                                        .createUser()
                        )
                );
        Mockito.when(authorDao.getAuthorsByBookId(1L))
                .thenReturn(List.of(
                                Author.builder()
                                        .setId(1L)
                                        .createAuthor()
                        )
                );
        Page<Order> orderPage = Page.<Order>builder()
                .setPageNumber(0)
                .setSearch("")
                .setSorting("ASC")
                .setLimit(5)
                .setElementsCount(1)
                .setData(List.of(order))
                .createPage();
        Mockito.when(orderDao.getPage(orderPage)).thenReturn(orderPage);
        OrderService orderService = new OrderService(daoFactory);
        Assertions.assertEquals("{" +
                        "\"elementsCount\":1," +
                        "\"limit\":5," +
                        "\"content\":[" +
                        "{\"id\":1,\"dateCreated\":\"1970-01-01\",\"dateExpire\":\"1970-01-01\"," +
                        "\"book\":{\"id\":1,\"name\":null,\"count\":0,\"publicationDate\":\"1970-01-01\",\"fine\":\"0.0 USD\",\"language\":null," +
                        "\"authors\":[{\"id\":1,\"firstName\":null,\"lastName\":null}]," +
                        "\"isbn\":null}," +
                        "\"user\":{\"login\":null,\"firstName\":null,\"lastName\":null,\"email\":null,\"phone\":null,\"fullName\":\"null null\"}," +
                        "\"place\":{\"name\":\"To the reading room\",\"data\":\"To the reading room\"}," +
                        "\"status\":{\"code\":\"ACCEPT\",\"name\":null,\"value\":\"Accepted\",\"nextStatuses\":[]}," +
                        "\"priceOverdue\":\"0.0 USD\",\"chooseDateExpire\":false}]}",
                orderService.getOrderPage(Locale.ENGLISH, orderPage));

        Mockito.when(bookDao.getBookByOrderId(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> orderService.getOrderPage(Locale.ENGLISH, orderPage));

        Mockito.when(daoFactory.createBookDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ServiceException.class, () -> orderService.getOrderPage(Locale.ENGLISH, orderPage));

        Mockito.when(daoFactory.createOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> orderService.getOrderPage(Locale.ENGLISH, orderPage));
    }

    @Test
    void updateOrder() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createOrderDao()).thenReturn(orderDao);
        Order order = Order.builder()
                .setId(1L)
                .setDateCreated(new java.util.Date(1))
                .setDateExpire(new java.util.Date(1))
                .setBook(Book.builder()
                        .setId(1L)
                        .setAuthors(
                                List.of(
                                        Author.builder()
                                                .setId(1L)
                                                .createAuthor()
                                )
                        )
                        .createBook()
                )
                .setUser(User.builder()
                        .setId(1L)
                        .createUser()
                )
                .setPlace(Place.builder()
                        .setId(1L)
                        .createPlace()
                )
                .setStatus(
                        Status.builder()
                                .setId(1L)
                                .createStatus()
                )
                .createOrder();
        orderService = new OrderService(daoFactory);
        Assertions.assertDoesNotThrow(() -> orderService.updateOrder(order));

        Mockito.doThrow(DaoException.class).when(orderDao).update(order);
        Assertions.assertThrows(ServiceException.class, () -> orderService.updateOrder(order));

        Mockito.doThrow(SQLException.class).when(orderDao).update(order);
        Assertions.assertThrows(ConnectionDBException.class, () -> orderService.updateOrder(order));

        Mockito.when(daoFactory.createOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> orderService.updateOrder(order));
    }

    @Test
    void deleteOrder() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createOrderDao()).thenReturn(orderDao);
        orderService = new OrderService(daoFactory);
        Assertions.assertDoesNotThrow(() -> orderService.deleteOrder(1L));

        Mockito.doThrow(SQLException.class).when(orderDao).delete(1L);
        Assertions.assertThrows(ConnectionDBException.class, () -> orderService.deleteOrder(1L));

        Mockito.doThrow(DaoException.class).when(orderDao).delete(1L);
        Assertions.assertThrows(ServiceException.class, () -> orderService.deleteOrder(1L));

        Mockito.when(daoFactory.createOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> orderService.deleteOrder(1L));
    }

    @Test
    void getOrderPageByBookId() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createOrderDao()).thenReturn(orderDao);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        Mockito.when(daoFactory.createStatusDao()).thenReturn(statusDao);
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Order order = Order.builder()
                .setId(1L)
                .setDateCreated(new java.util.Date(1))
                .setDateExpire(new java.util.Date(1))
                .setBook(Book.builder()
                        .setId(1L)
                        .setAuthors(
                                List.of(
                                        Author.builder()
                                                .setId(1L)
                                                .createAuthor()
                                )
                        )
                        .createBook()
                )
                .setUser(User.builder()
                        .setId(1L)
                        .createUser()
                )
                .setPlace(Place.builder()
                        .setId(1L)
                        .createPlace()
                )
                .setStatus(
                        Status.builder()
                                .setId(1L)
                                .createStatus()
                )
                .createOrder();
        Mockito.when(bookDao.getBookByOrderId(1L))
                .thenReturn(Optional.of(
                                Book.builder()
                                        .setId(1L)
                                        .setPublicationDate(new java.util.Date(1))
                                        .createBook()
                        )
                );
        Mockito.when(placeDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                Place.builder()
                                        .setId(1L)
                                        .setName("To the reading room")
                                        .createPlace()
                        )
                );
        Mockito.when(statusDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                Status.builder()
                                        .setId(1L)
                                        .setCode("ACCEPT")
                                        .createStatus()
                        )
                );
        Mockito.when(userDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                User.builder()
                                        .setId(1L)
                                        .createUser()
                        )
                );
        Mockito.when(authorDao.getAuthorsByBookId(1L))
                .thenReturn(List.of(
                                Author.builder()
                                        .setId(1L)
                                        .createAuthor()
                        )
                );
        Page<Order> orderPage = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .createPage();
        Page<Order> resultPage = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(order))
                .createPage();
        Mockito.when(orderDao.getPageByBookId(orderPage, 1L)).thenReturn(resultPage);
        orderService = new OrderService(daoFactory);
        Assertions.assertEquals("{" +
                        "\"elementsCount\":1," +
                        "\"limit\":5," +
                        "\"content\":[" +
                        "{\"id\":1,\"dateCreated\":\"1970-01-01\",\"dateExpire\":\"1970-01-01\"," +
                        "\"book\":{\"id\":1,\"name\":null,\"count\":0,\"publicationDate\":\"1970-01-01\",\"fine\":\"0.0 USD\"," +
                        "\"language\":null," +
                        "\"authors\":[{\"id\":1,\"firstName\":null,\"lastName\":null}]," +
                        "\"isbn\":null}," +
                        "\"user\":{\"login\":null,\"firstName\":null,\"lastName\":null,\"email\":null,\"phone\":null,\"fullName\":\"null null\"}," +
                        "\"place\":{\"name\":\"To the reading room\",\"data\":\"To the reading room\"}," +
                        "\"status\":{\"code\":\"ACCEPT\",\"name\":null,\"value\":\"Accepted\",\"nextStatuses\":[]}," +
                        "\"priceOverdue\":\"0.0 USD\",\"chooseDateExpire\":false}]}",
                orderService.getOrderPageByBookId(Locale.ENGLISH, orderPage, 1L));

        Mockito.when(orderDao.getPageByBookId(orderPage, 1L)).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> orderService.getOrderPageByBookId(Locale.ENGLISH, orderPage, 1L));

        Mockito.when(daoFactory.createOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> orderService.getOrderPageByBookId(Locale.ENGLISH, orderPage, 1L));
    }

    @Test
    void getOrderPageByUserIdAndStatusId() throws ServiceException, JDBCException, ConnectionDBException {
        Mockito.when(daoFactory.createOrderDao()).thenReturn(orderDao);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        Mockito.when(daoFactory.createStatusDao()).thenReturn(statusDao);
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Order order = Order.builder()
                .setId(1L)
                .setDateCreated(new java.util.Date(1))
                .setDateExpire(new java.util.Date(1))
                .setBook(Book.builder()
                        .setId(1L)
                        .setAuthors(
                                List.of(
                                        Author.builder()
                                                .setId(1L)
                                                .createAuthor()
                                )
                        )
                        .createBook()
                )
                .setUser(User.builder()
                        .setId(1L)
                        .createUser()
                )
                .setPlace(Place.builder()
                        .setId(1L)
                        .createPlace()
                )
                .setStatus(
                        Status.builder()
                                .setId(1L)
                                .createStatus()
                )
                .createOrder();
        Mockito.when(bookDao.getBookByOrderId(1L))
                .thenReturn(Optional.of(
                                Book.builder()
                                        .setId(1L)
                                        .setPublicationDate(new java.util.Date(1))
                                        .createBook()
                        )
                );
        Mockito.when(placeDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                Place.builder()
                                        .setId(1L)
                                        .setName("To the reading room")
                                        .createPlace()
                        )
                );
        Mockito.when(statusDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                Status.builder()
                                        .setId(1L)
                                        .setCode("ACCEPT")
                                        .createStatus()
                        )
                );
        Mockito.when(userDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                User.builder()
                                        .setId(1L)
                                        .createUser()
                        )
                );
        Mockito.when(authorDao.getAuthorsByBookId(1L))
                .thenReturn(List.of(
                                Author.builder()
                                        .setId(1L)
                                        .createAuthor()
                        )
                );
        Page<Order> orderPage = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .createPage();
        Page<Order> resultPage = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(order))
                .createPage();
        Mockito.when(orderDao.getPageByStatusAndUserId(orderPage, 1L, 1L)).thenReturn(resultPage);
        orderService = new OrderService(daoFactory);
        Assertions.assertEquals("{" +
                        "\"elementsCount\":1," +
                        "\"limit\":5," +
                        "\"content\":[" +
                        "{\"id\":1,\"dateCreated\":\"1970-01-01\",\"dateExpire\":\"1970-01-01\"," +
                        "\"book\":{\"id\":1,\"name\":null,\"count\":0,\"publicationDate\":\"1970-01-01\",\"fine\":\"0.0 USD\",\"language\":null," +
                        "\"authors\":[{\"id\":1,\"firstName\":null,\"lastName\":null}],\"isbn\":null}," +
                        "\"user\":{\"login\":null,\"firstName\":null,\"lastName\":null,\"email\":null,\"phone\":null,\"fullName\":\"null null\"}," +
                        "\"place\":{\"name\":\"To the reading room\",\"data\":\"To the reading room\"}," +
                        "\"status\":{\"code\":\"ACCEPT\",\"name\":null,\"value\":\"Accepted\",\"nextStatuses\":[]}," +
                        "\"priceOverdue\":\"0.0 USD\",\"chooseDateExpire\":false}]}",
                orderService.getOrderPageByUserIdAndStatusId(Locale.ENGLISH, orderPage, 1L, 1L));

        Mockito.when(orderDao.getPageByStatusAndUserId(orderPage, 1L, 1L)).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> orderService.getOrderPageByUserIdAndStatusId(Locale.ENGLISH, orderPage, 1L, 1L));

        Mockito.when(daoFactory.createOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> orderService.getOrderPageByUserIdAndStatusId(Locale.ENGLISH, orderPage, 1L, 1L));
    }

    @Test
    void getOrderPageByStatusId() throws ServiceException, JDBCException, ConnectionDBException {
        Mockito.when(daoFactory.createOrderDao()).thenReturn(orderDao);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        Mockito.when(daoFactory.createStatusDao()).thenReturn(statusDao);
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Order order = Order.builder()
                .setId(1L)
                .setDateCreated(new java.util.Date(1))
                .setDateExpire(new java.util.Date(1))
                .setBook(Book.builder()
                        .setId(1L)
                        .setAuthors(
                                List.of(
                                        Author.builder()
                                                .setId(1L)
                                                .createAuthor()
                                )
                        )
                        .createBook()
                )
                .setUser(User.builder()
                        .setId(1L)
                        .createUser()
                )
                .setPlace(Place.builder()
                        .setId(1L)
                        .createPlace()
                )
                .setStatus(
                        Status.builder()
                                .setId(1L)
                                .createStatus()
                )
                .createOrder();
        Mockito.when(bookDao.getBookByOrderId(1L))
                .thenReturn(Optional.of(
                                Book.builder()
                                        .setId(1L)
                                        .setPublicationDate(new java.util.Date(1))
                                        .createBook()
                        )
                );
        Mockito.when(placeDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                Place.builder()
                                        .setId(1L)
                                        .setName("To the reading room")
                                        .createPlace()
                        )
                );
        Mockito.when(statusDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                Status.builder()
                                        .setId(1L)
                                        .setCode("ACCEPT")
                                        .createStatus()
                        )
                );
        Mockito.when(userDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                User.builder()
                                        .setId(1L)
                                        .createUser()
                        )
                );
        Mockito.when(authorDao.getAuthorsByBookId(1L))
                .thenReturn(List.of(
                                Author.builder()
                                        .setId(1L)
                                        .createAuthor()
                        )
                );
        Page<Order> orderPage = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .createPage();
        Page<Order> resultPage = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(order))
                .createPage();
        Mockito.when(orderDao.getPageByStatusId(orderPage, 1L, "book_name")).thenReturn(resultPage);
        orderService = new OrderService(daoFactory);
        Assertions.assertEquals("{" +
                        "\"elementsCount\":1," +
                        "\"limit\":5," +
                        "\"content\":[" +
                        "{\"id\":1,\"dateCreated\":\"1970-01-01\",\"dateExpire\":\"1970-01-01\"," +
                        "\"book\":{\"id\":1,\"name\":null,\"count\":0,\"publicationDate\":\"1970-01-01\",\"fine\":\"0.0 USD\",\"language\":null," +
                        "\"authors\":[{\"id\":1,\"firstName\":null,\"lastName\":null}],\"isbn\":null}," +
                        "\"user\":{\"login\":null,\"firstName\":null,\"lastName\":null,\"email\":null,\"phone\":null,\"fullName\":\"null null\"}," +
                        "\"place\":{\"name\":\"To the reading room\",\"data\":\"To the reading room\"}," +
                        "\"status\":{\"code\":\"ACCEPT\",\"name\":null,\"value\":\"Accepted\",\"nextStatuses\":[]}," +
                        "\"priceOverdue\":\"0.0 USD\",\"chooseDateExpire\":false}]}",
                orderService.getOrderPageByStatusId(Locale.ENGLISH, orderPage, 1L, "book_name"));

        Mockito.when(orderDao.getPageByStatusId(orderPage, 1L, "book_name")).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> orderService.getOrderPageByStatusId(Locale.ENGLISH, orderPage, 1L, "book_name"));

        Mockito.when(daoFactory.createOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> orderService.getOrderPageByStatusId(Locale.ENGLISH, orderPage, 1L, "book_name"));
    }

    @Test
    void getOrderPageByStatusIdAndPlaceId() throws JDBCException, ServiceException, ConnectionDBException {
        Mockito.when(daoFactory.createOrderDao()).thenReturn(orderDao);
        Mockito.when(daoFactory.createBookDao()).thenReturn(bookDao);
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        Mockito.when(daoFactory.createStatusDao()).thenReturn(statusDao);
        Mockito.when(daoFactory.createUserDao()).thenReturn(userDao);
        Mockito.when(daoFactory.createAuthorDao()).thenReturn(authorDao);
        Order order = Order.builder()
                .setId(1L)
                .setDateCreated(new java.util.Date(1))
                .setDateExpire(new java.util.Date(1))
                .setBook(Book.builder()
                        .setId(1L)
                        .setAuthors(
                                List.of(
                                        Author.builder()
                                                .setId(1L)
                                                .createAuthor()
                                )
                        )
                        .createBook()
                )
                .setUser(User.builder()
                        .setId(1L)
                        .createUser()
                )
                .setPlace(Place.builder()
                        .setId(1L)
                        .createPlace()
                )
                .setStatus(
                        Status.builder()
                                .setId(1L)
                                .createStatus()
                )
                .createOrder();
        Mockito.when(bookDao.getBookByOrderId(1L))
                .thenReturn(Optional.of(
                                Book.builder()
                                        .setId(1L)
                                        .setPublicationDate(new java.util.Date(1))
                                        .createBook()
                        )
                );
        Mockito.when(placeDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                Place.builder()
                                        .setId(1L)
                                        .setName("To the reading room")
                                        .createPlace()
                        )
                );
        Mockito.when(statusDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                Status.builder()
                                        .setId(1L)
                                        .setCode("ACCEPT")
                                        .createStatus()
                        )
                );
        Mockito.when(userDao.getByOrderId(1L))
                .thenReturn(Optional.of(
                                User.builder()
                                        .setId(1L)
                                        .createUser()
                        )
                );
        Mockito.when(authorDao.getAuthorsByBookId(1L))
                .thenReturn(List.of(
                                Author.builder()
                                        .setId(1L)
                                        .createAuthor()
                        )
                );
        Page<Order> orderPage = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .createPage();
        Page<Order> resultPage = Page.<Order>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(order))
                .createPage();
        Mockito.when(orderDao.getPageByStatusIdAndPlaceId(orderPage, 1L, 1L, "book_name")).thenReturn(resultPage);
        orderService = new OrderService(daoFactory);
        Assertions.assertEquals("{" +
                        "\"elementsCount\":1," +
                        "\"limit\":5," +
                        "\"content\":[" +
                        "{\"id\":1,\"dateCreated\":\"1970-01-01\",\"dateExpire\":\"1970-01-01\"," +
                        "\"book\":{\"id\":1,\"name\":null,\"count\":0,\"publicationDate\":\"1970-01-01\",\"fine\":\"0.0 USD\",\"language\":null," +
                        "\"authors\":[{\"id\":1,\"firstName\":null,\"lastName\":null}],\"isbn\":null}," +
                        "\"user\":{\"login\":null,\"firstName\":null,\"lastName\":null,\"email\":null,\"phone\":null,\"fullName\":\"null null\"}," +
                        "\"place\":{\"name\":\"To the reading room\",\"data\":\"To the reading room\"}," +
                        "\"status\":{\"code\":\"ACCEPT\",\"name\":null,\"value\":\"Accepted\",\"nextStatuses\":[]}," +
                        "\"priceOverdue\":\"0.0 USD\",\"chooseDateExpire\":false}]}",
                orderService.getOrderPageByStatusIdAndPlaceId(Locale.ENGLISH, orderPage, 1L, 1L, "book_name"));

        Mockito.when(orderDao.getPageByStatusIdAndPlaceId(orderPage, 1L, 1L, "book_name")).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> orderService.getOrderPageByStatusIdAndPlaceId(Locale.ENGLISH, orderPage, 1L, 1L, "book_name"));

        Mockito.when(daoFactory.createOrderDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> orderService.getOrderPageByStatusIdAndPlaceId(Locale.ENGLISH, orderPage, 1L, 1L, "book_name"));
    }
}
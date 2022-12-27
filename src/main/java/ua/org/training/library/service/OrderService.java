package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.*;
import ua.org.training.library.dto.OrderDTO;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.Order;
import ua.org.training.library.model.Status;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class OrderService {
    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public OrderService() {
    }

    public OrderService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public long createOrder(Order order) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            return orderDao.create(order);
        } catch (SQLException e) {
            LOGGER.error("Error while creating order", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while creating order", e);
        } catch (DaoException e) {
            LOGGER.error("Error while creating order", e);
            throw new ServiceException("Error while creating order", e);
        }
        return Constants.INVALID_ID;
    }

    public Order getOrderById(long id) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            Order order = orderDao.getById(id).orElseThrow(() -> new ServiceException("Order not found"));
            return loadFields(order);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting order by id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting order by id", e);
            throw new ServiceException("Error while getting order by id", e);
        }
        return null;
    }

    private Order loadFields(Order order) throws ServiceException {
        try {
            try (BookDao bookDao = daoFactory.createBookDao()) {
                Book book = bookDao.getBookByOrderId(order.getId()).orElseThrow(() -> new ServiceException("Book not found"));
                try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
                    book.setAuthors(authorDao.getAuthorsByBookId(book.getId()));
                }
                order.setBook(book);
            }
            try (UserDao userDao = daoFactory.createUserDao()) {
                order.setUser(userDao.getByOrderId(order.getId()).orElseThrow(() -> new ServiceException("User not found")));
            }
            try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
                order.setPlace(placeDao.getByOrderId(order.getId()).orElseThrow(() -> new ServiceException("Place not found")));
            }
            try (StatusDao statusDao = daoFactory.createStatusDao()) {
                Status status = statusDao.getByOrderId(order.getId()).orElseThrow(() -> new ServiceException("Status not found"));
                status.setNextStatuses(statusDao.getNextStatusesForStatusById(status.getId()));
                order.setStatus(status);
            }
        } catch (JDBCException e) {
            LOGGER.error("Error while loading fields", e);
        }
        return order;
    }

    public String getOrderPage(Locale locale, Page<Order> orderPage) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            PageService<OrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, orderDao.getPage(orderPage)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting order page", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting order page", e);
            throw new ServiceException("Error while getting order page", e);
        }
        return null;
    }

    private Page<OrderDTO> getDtoPage(Locale locale, Page<Order> orderPage) {
        return Page.<OrderDTO>builder()
                .setPageNumber(orderPage.getPageNumber())
                .setLimit(orderPage.getLimit())
                .setSorting(orderPage.getSorting())
                .setSearch(orderPage.getSearch())
                .setElementsCount(orderPage.getElementsCount())
                .setData(formatDataOrder(locale, orderPage.getData()))
                .createPage();
    }

    private List<OrderDTO> formatDataOrder(Locale locale, List<Order> pageData){
        return pageData.stream().map(order -> {
            try {
                return new OrderDTO(locale, loadFields(order));
            } catch (ServiceException e) {
                LOGGER.error("Error while formatting data", e);
            }
            return null;
        }).toList();
    }

    public void updateOrder(Order order) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            orderDao.update(order);
        } catch (SQLException e) {
            LOGGER.error("Error while updating order", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while updating order", e);
        } catch (DaoException e) {
            LOGGER.error("Error while updating order", e);
            throw new ServiceException("Error while updating order", e);
        }
    }

    public void deleteOrder(long id) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            orderDao.delete(id);
        } catch (SQLException e) {
            LOGGER.error("Error while deleting order", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while deleting order", e);
        } catch (DaoException e) {
            LOGGER.error("Error while deleting order", e);
            throw new ServiceException("Error while deleting order", e);
        }
    }

    public String getOrderPageByUserId(Locale locale, Page<Order> orderPage, long userId) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            PageService<OrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, orderDao.getPageByUserId(orderPage, userId)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting order page by user id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting order page by user id", e);
            throw new ServiceException("Error while getting order page by user id", e);
        }
        return null;
    }

    public String getOrderPageByPlaceId(Locale locale, Page<Order> orderPage, long placeId) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            PageService<OrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, orderDao.getPageByPlaceId(orderPage, placeId)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting order page by place id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting order page by place id", e);
            throw new ServiceException("Error while getting order page by place id", e);
        }
        return null;
    }

    public String getOrderPageByUserIdAndPlaceId(Locale locale, Page<Order> orderPage, long userId, long placeId) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            PageService<OrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, orderDao.getPageByUserIdAndPlaceId(orderPage, userId, placeId)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting order page by user id and place id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting order page by user id and place id", e);
            throw new ServiceException("Error while getting order page by user id and place id", e);
        }
        return null;
    }

    public String getOrderPageByPlaceName(Locale locale, Page<Order> orderPage, String placeName) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            PageService<OrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, orderDao.getPageByPlaceName(orderPage, placeName)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting order page by place name", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting order page by place name", e);
            throw new ServiceException("Error while getting order page by place name", e);
        }
        return null;
    }

    public String getOrderPageByBookId(Locale locale, Page<Order> orderPage, long bookId) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            PageService<OrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, orderDao.getPageByBookId(orderPage, bookId)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting order page by book id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting order page by book id", e);
            throw new ServiceException("Error while getting order page by book id", e);
        }
        return null;
    }

    public String getOrderPageByUserIdAndStatusId(Locale locale, Page<Order> orderPage, long userId, long statusId) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            PageService<OrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, orderDao.getPageByStatusAndUserId(orderPage, statusId, userId)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting order page by user id and status id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting order page by user id and status id", e);
            throw new ServiceException("Error while getting order page by user id and status id", e);
        }
        return null;
    }

    public String getOrderPageByStatusId(Locale locale, Page<Order> page, long id, String sortBy) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            PageService<OrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, orderDao.getPageByStatusId(page, id, sortBy)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting order page by status id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting order page by status id", e);
            throw new ServiceException("Error while getting order page by status id", e);
        }
        return null;
    }


    public String getOrderPageByStatusIdAndPlaceId(Locale locale, Page<Order> page, long statusId, long placeId, String sortBy) throws ServiceException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            PageService<OrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, orderDao.getPageByStatusIdAndPlaceId(page, statusId, placeId, sortBy)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting order page by status id and place id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting order page by status id and place id", e);
            throw new ServiceException("Error while getting order page by status id and place id", e);
        }
        return null;
    }
}

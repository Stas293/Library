package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.HistoryOrderDao;
import ua.org.training.library.dao.StatusDao;
import ua.org.training.library.dao.UserDao;
import ua.org.training.library.dto.HistoryOrderDTO;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class HistoryOrderService {
    private static final Logger LOGGER = LogManager.getLogger(HistoryOrderService.class);
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public HistoryOrderService() {
    }

    public HistoryOrderService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public long createHistoryOrder(HistoryOrder historyOrder) throws ServiceException {
        try (HistoryOrderDao historyOrderDao = daoFactory.createHistoryOrderDao()) {
            return historyOrderDao.create(historyOrder);
        } catch (SQLException e) {
            LOGGER.error("Error while creating historyOrder", e);
            throw new ServiceException("Error while creating historyOrder", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while creating historyOrder", e);
        } catch (DaoException e) {
            LOGGER.error("Error while creating historyOrder", e);
            throw new ServiceException("Error while creating historyOrder", e);
        }
        return 0;
    }

    public HistoryOrder getHistoryOrderById(long id) throws ServiceException {
        try (HistoryOrderDao historyOrderDao = daoFactory.createHistoryOrderDao()) {
            return historyOrderDao.getById(id).orElseThrow(() -> new ServiceException("HistoryOrder not found"));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting historyOrder by id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting historyOrder by id", e);
            throw new ServiceException("Error while getting historyOrder by id", e);
        }
        return null;
    }

    public String getHistoryOrderPage(Locale locale, Page<HistoryOrder> historyOrderPage) throws ServiceException {
        try (HistoryOrderDao historyOrderDao = daoFactory.createHistoryOrderDao()) {
            PageService<HistoryOrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, historyOrderDao.getPage(historyOrderPage)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting historyOrder page", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting historyOrder page", e);
            throw new ServiceException("Error while getting historyOrder page", e);
        }
        return null;
    }

    private Page<HistoryOrderDTO> getDtoPage(Locale locale, Page<HistoryOrder> historyOrderPage) {
        return Page.<HistoryOrderDTO>builder()
                .setPageNumber(historyOrderPage.getPageNumber())
                .setLimit(historyOrderPage.getLimit())
                .setSorting(historyOrderPage.getSorting())
                .setSearch(historyOrderPage.getSearch())
                .setElementsCount(historyOrderPage.getElementsCount())
                .setData(formatDataHistoryOrder(locale, historyOrderPage.getData()))
                .createPage();
    }

    private List<HistoryOrderDTO> formatDataHistoryOrder(Locale locale, List<HistoryOrder> historyOrders) {
        return historyOrders.stream().map(historyOrder -> new HistoryOrderDTO(locale, loadFields(historyOrder))).toList();
    }

    private HistoryOrder loadFields(HistoryOrder historyOrder) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            historyOrder.setUser(userDao.getByHistoryOrderId(historyOrder.getId()).orElse(null));
            try (StatusDao statusDao = daoFactory.createStatusDao()) {
                historyOrder.setStatus(statusDao.getByHistoryOrderId(historyOrder.getId()).orElse(null));
            }
        } catch (JDBCException e) {
            LOGGER.error("Error while loading fields", e);
        } catch (DaoException e) {
            LOGGER.error("Error while loading fields", e);
        }
        return historyOrder;
    }

    public void updateHistoryOrder(HistoryOrder historyOrder) throws ServiceException {
        try (HistoryOrderDao historyOrderDao = daoFactory.createHistoryOrderDao()) {
            historyOrderDao.update(historyOrder);
        } catch (SQLException e) {
            LOGGER.error("Error while updating historyOrder", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while updating historyOrder", e);
        } catch (DaoException e) {
            LOGGER.error("Error while updating historyOrder", e);
            throw new ServiceException("Error while updating historyOrder", e);
        }
    }

    public void deleteHistoryOrder(long id) throws ServiceException {
        try (HistoryOrderDao historyOrderDao = daoFactory.createHistoryOrderDao()) {
            historyOrderDao.delete(id);
        } catch (SQLException e) {
            LOGGER.error("Error while deleting historyOrder", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while deleting historyOrder", e);
        } catch (DaoException e) {
            LOGGER.error("Error while deleting historyOrder", e);
            throw new ServiceException("Error while deleting historyOrder", e);
        }
    }

    public String getHistoryOrderPageByUserId(Locale locale, Page<HistoryOrder> historyOrderPage, long userId) throws ServiceException {
        try (HistoryOrderDao historyOrderDao = daoFactory.createHistoryOrderDao()) {
            PageService<HistoryOrderDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, historyOrderDao.getPageByUserId(historyOrderPage, userId)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting historyOrder page by user id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting historyOrder page by user id", e);
            throw new ServiceException("Error while getting historyOrder page by user id", e);
        }
        return null;
    }
}

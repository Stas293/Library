package ua.org.training.library.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.PlaceDao;
import ua.org.training.library.dto.OrderDTO;
import ua.org.training.library.dto.PlaceDTO;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Place;
import ua.org.training.library.utility.DTOMapper;
import ua.org.training.library.utility.ListService;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PlaceService {
    private static final Logger LOGGER = LogManager.getLogger(PlaceService.class);
    private final DaoFactory daoFactory;

    public PlaceService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public long createPlace(Place place) throws ServiceException, ConnectionDBException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            return placeDao.create(place);
        } catch (SQLException | JDBCException e) {
            LOGGER.error("Error while creating place", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while creating place", e);
            throw new ServiceException("Error while creating place", e);
        }
    }

    public Place getPlaceById(long id) throws ServiceException, ConnectionDBException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            return placeDao.getById(id).orElseThrow(() -> new ServiceException("Place not found"));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting place by id", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting place by id", e);
            throw new ServiceException("Error while getting place by id", e);
        }
    }

    public String getPlacePage(Locale locale, Page<Place> placePage) throws ServiceException, ConnectionDBException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            PageService<PlaceDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, placeDao.getPage(placePage)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting place page", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting place page", e);
            throw new ServiceException("Error while getting place page", e);
        }
    }

    public String getPlaceList(Locale locale) throws ServiceException, ConnectionDBException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            return new ListService<PlaceDTO>().jsonifyList(formatDataPlace(locale, placeDao.getAll()));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting place list", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting place list", e);
            throw new ServiceException("Error while getting place list", e);
        }
    }

    private Page<PlaceDTO> getDtoPage(Locale locale, Page<Place> placePage) {
        return Page.<PlaceDTO>builder()
                .setPageNumber(placePage.getPageNumber())
                .setLimit(placePage.getLimit())
                .setSorting(placePage.getSorting())
                .setSearch(placePage.getSearch())
                .setElementsCount(placePage.getElementsCount())
                .setData(formatDataPlace(locale, placePage.getData()))
                .createPage();
    }

    private List<PlaceDTO> formatDataPlace(Locale locale, List<Place> pageData) {
        return pageData.stream().map(place -> DTOMapper.placeToDTO(locale, place)).toList();
    }

    public void updatePlace(Place place) throws ServiceException, ConnectionDBException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            placeDao.update(place);
        } catch (SQLException | JDBCException e) {
            LOGGER.error("Error while updating place", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while updating place", e);
            throw new ServiceException("Error while updating place", e);
        }
    }

    public void deletePlace(long id) throws ServiceException, ConnectionDBException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            placeDao.delete(id);
        } catch (SQLException | JDBCException e) {
            LOGGER.error("Error while deleting place", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while deleting place", e);
            throw new ServiceException("Error while deleting place", e);
        }
    }

    public Place getPlaceByName(String name) throws ServiceException, ConnectionDBException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            return placeDao.getByName(name).orElseThrow(() -> new ServiceException("Place not found"));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting place by name", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting place by name", e);
            throw new ServiceException("Error while getting place by name", e);
        }
    }
}

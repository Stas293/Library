package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.PlaceDao;
import ua.org.training.library.dto.OrderDTO;
import ua.org.training.library.dto.PlaceDTO;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Place;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class PlaceService {
    private static final Logger LOGGER = LogManager.getLogger(PlaceService.class);
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public PlaceService() {
    }

    public PlaceService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public long createPlace(Place place) throws ServiceException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            return placeDao.create(place);
        } catch (SQLException e) {
            LOGGER.error("Error while creating place", e);
            throw new ServiceException("Error while creating place", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while creating place", e);
        } catch (DaoException e) {
            LOGGER.error("Error while creating place", e);
            throw new ServiceException("Error while creating place", e);
        }
        return 0;
    }

    public Place getPlaceById(long id) throws ServiceException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            return placeDao.getById(id).orElseThrow(() -> new ServiceException("Place not found"));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting place by id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting place by id", e);
            throw new ServiceException("Error while getting place by id", e);
        }
        return null;
    }

    public String getPlacePage(Locale locale, Page<Place> placePage) throws ServiceException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            PageService<PlaceDTO> pageService = new PageService<>();
            return pageService.jsonifyPage(getDtoPage(locale, placeDao.getPage(placePage)));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting place page", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting place page", e);
            throw new ServiceException("Error while getting place page", e);
        }
        return null;
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
        return pageData.stream().map(place -> new PlaceDTO(locale, place)).toList();
    }

    public void updatePlace(Place place) throws ServiceException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            placeDao.update(place);
        } catch (SQLException e) {
            LOGGER.error("Error while updating place", e);
            throw new ServiceException("Error while updating place", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while updating place", e);
        } catch (DaoException e) {
            LOGGER.error("Error while updating place", e);
            throw new ServiceException("Error while updating place", e);
        }
    }

    public void deletePlace(long id) throws ServiceException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            placeDao.delete(id);
        } catch (SQLException e) {
            LOGGER.error("Error while deleting place", e);
            throw new ServiceException("Error while deleting place", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while deleting place", e);
        } catch (DaoException e) {
            LOGGER.error("Error while deleting place", e);
            throw new ServiceException("Error while deleting place", e);
        }
    }

    public Place getPlaceByName(String name) throws ServiceException {
        try (PlaceDao placeDao = daoFactory.createPlaceDao()) {
            return placeDao.getByName(name).orElseThrow(() -> new ServiceException("Place not found"));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting place by name", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting place by name", e);
            throw new ServiceException("Error while getting place by name", e);
        }
        return null;
    }
}

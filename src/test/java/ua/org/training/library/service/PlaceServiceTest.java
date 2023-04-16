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
import ua.org.training.library.model.Place;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private PlaceDao placeDao;
    private PlaceService placeService;

    @Test
    void createPlace() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        placeService = new PlaceService(daoFactory);
        Place place = Place.builder()
                .setName("To the reading room")
                .createPlace();
        Mockito.when(placeDao.create(place)).thenReturn(1L);
        assertEquals(1L, placeService.createPlace(place));

        Mockito.when(placeDao.create(place)).thenThrow(SQLException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> placeService.createPlace(place));

        placeDao = Mockito.mock(PlaceDao.class);
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        Mockito.when(placeDao.create(place)).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> placeService.createPlace(place));

        Mockito.when(daoFactory.createPlaceDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> placeService.createPlace(place));
    }

    @Test
    void getPlaceById() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        placeService = new PlaceService(daoFactory);
        Place place = Place.builder()
                .setId(1L)
                .setName("To the reading room")
                .createPlace();
        Mockito.when(placeDao.getById(1L)).thenReturn(java.util.Optional.of(place));
        assertEquals(place, placeService.getPlaceById(1L));

        Mockito.when(placeDao.getById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> placeService.getPlaceById(1L));

        Mockito.when(placeDao.getById(1L)).thenThrow(DaoException.class);
        Assertions.assertThrows(ServiceException.class, () -> placeService.getPlaceById(1L));

        Mockito.when(daoFactory.createPlaceDao()).thenThrow(JDBCException.class);
        Assertions.assertThrows(ConnectionDBException.class, () -> placeService.getPlaceById(1L));
    }

    @Test
    void getPlacePage() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        placeService = new PlaceService(daoFactory);
        Place place = Place.builder()
                .setId(1L)
                .setName("To the reading room")
                .createPlace();
        Page<Place> placePage = Page.<Place>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .createPage();
        Page<Place> resultPage = Page.<Place>builder()
                .setPageNumber(0)
                .setLimit(5)
                .setSearch("")
                .setSorting("ASC")
                .setElementsCount(1)
                .setData(List.of(place))
                .createPage();
        Mockito.when(placeDao.getPage(placePage)).thenReturn(resultPage);
        assertEquals("{" +
                "\"elementsCount\":1," +
                "\"limit\":5," +
                "\"content\":[{\"name\":\"To the reading room\",\"data\":\"To the reading room\"}]}",
                placeService.getPlacePage(Locale.ENGLISH, placePage));

        Mockito.when(placeDao.getPage(placePage)).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> placeService.getPlacePage(Locale.ENGLISH, placePage));

        Mockito.when(daoFactory.createPlaceDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> placeService.getPlacePage(Locale.ENGLISH, placePage));
    }

    @Test
    void getPlaceList() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        placeService = new PlaceService(daoFactory);
        Place place = Place.builder()
                .setId(1L)
                .setName("To the reading room")
                .createPlace();
        Mockito.when(placeDao.getAll()).thenReturn(List.of(place));

        assertEquals("[" +
                "{\"name\":\"To the reading room\",\"data\":\"To the reading room\"}" +
                "]", placeService.getPlaceList(Locale.ENGLISH));

        Mockito.when(placeDao.getAll()).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> placeService.getPlaceList(Locale.ENGLISH));

        Mockito.when(daoFactory.createPlaceDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> placeService.getPlaceList(Locale.ENGLISH));
    }

    @Test
    void updatePlace() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        placeService = new PlaceService(daoFactory);
        Place place = Place.builder()
                .setId(1L)
                .setName("To the reading room")
                .createPlace();
        assertDoesNotThrow(() -> placeService.updatePlace(place));

        Mockito.doThrow(DaoException.class).when(placeDao).update(place);
        assertThrows(ServiceException.class, () -> placeService.updatePlace(place));

        Mockito.doThrow(SQLException.class).when(placeDao).update(place);
        assertThrows(ConnectionDBException.class, () -> placeService.updatePlace(place));

        Mockito.when(daoFactory.createPlaceDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> placeService.updatePlace(place));
    }

    @Test
    void deletePlace() throws JDBCException, ServiceException, SQLException {
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        placeService = new PlaceService(daoFactory);
        assertDoesNotThrow(() -> placeService.deletePlace(1L));

        Mockito.doThrow(DaoException.class).when(placeDao).delete(1L);
        assertThrows(ServiceException.class, () -> placeService.deletePlace(1L));

        Mockito.doThrow(SQLException.class).when(placeDao).delete(1L);
        assertThrows(ConnectionDBException.class, () -> placeService.deletePlace(1L));

        Mockito.when(daoFactory.createPlaceDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> placeService.deletePlace(1L));
    }

    @Test
    void getPlaceByName() throws JDBCException, ServiceException, SQLException, ConnectionDBException {
        Mockito.when(daoFactory.createPlaceDao()).thenReturn(placeDao);
        placeService = new PlaceService(daoFactory);
        Place place = Place.builder()
                .setId(1L)
                .setName("On the subscription")
                .createPlace();
        Mockito.when(placeDao.getByName("On the subscription")).thenReturn(Optional.ofNullable(place));
        assertEquals(place, placeService.getPlaceByName("On the subscription"));

        Mockito.when(placeDao.getByName("On the subscription")).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> placeService.getPlaceByName("On the subscription"));

        Mockito.when(daoFactory.createPlaceDao()).thenThrow(JDBCException.class);
        assertThrows(ConnectionDBException.class, () -> placeService.getPlaceByName("On the subscription"));
    }
}
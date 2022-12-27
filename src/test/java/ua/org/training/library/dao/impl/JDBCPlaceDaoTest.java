package ua.org.training.library.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.PlaceDao;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.model.Place;
import ua.org.training.library.utility.page.Page;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JDBCPlaceDaoTest {
    PlaceDao placeDao;
    @BeforeEach
    void setUp() {
        try {
            placeDao = DaoFactory.getInstance().createPlaceDao();
        } catch (JDBCException e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() throws SQLException {
        Place place = new Place();
        place.setName("Test");
        long id = placeDao.create(place);
        place.setId(id);
        setUp();
        assertEquals(place, placeDao.getById(id).get());
    }

    @Test
    void getById() {
        Place place = new Place();
        place.setId(1L);
        place.setName("Test");
        assertEquals(Optional.of(place), placeDao.getById(1L));
    }

    @Test
    void getPage() {
        Place place = new Place();
        place.setId(1L);
        place.setName("Test");
        assertEquals(Page.<Place> builder()
                .setPageNumber(0)
                .setLimit(10)
                .setElementsCount(1)
                .setData(List.of(place))
                .setSorting("ASC")
                .createPage(), placeDao.getPage(Page.<Place> builder()
                .setPageNumber(0)
                .setLimit(10)
                .setSorting("ASC")
                .createPage()));
    }

    @Test
    void update() throws SQLException {
        Place place = new Place();
        place.setId(1L);
        place.setName("Test");
        placeDao.update(place);
        setUp();
        assertEquals(place, placeDao.getById(1L).get());
    }

    @Test
    void delete() throws SQLException {
        placeDao.delete(1L);
        setUp();
        assertFalse(placeDao.getById(1L).isPresent());
    }

    @Test
    void getByOrderId() {
        Place place = new Place();
        place.setId(1L);
        place.setName("On a subscription");
        assertEquals(Optional.of(place), placeDao.getByOrderId(1L));
    }

    @Test
    void getByName() {
        Place place = new Place();
        place.setId(1L);
        place.setName("On a subscription");
        assertEquals(Optional.of(place), placeDao.getByName("On a subscription"));
    }
}
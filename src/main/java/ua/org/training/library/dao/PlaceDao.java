package ua.org.training.library.dao;


import ua.org.training.library.model.Place;

import java.sql.Connection;
import java.util.Optional;

public interface PlaceDao extends GenericDao<Place> {
    Optional<Place> getByOrderId(Connection connection, Long id);

    Optional<Place> getByCode(Connection connection, String name);
}

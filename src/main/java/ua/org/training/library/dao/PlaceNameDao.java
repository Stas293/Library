package ua.org.training.library.dao;


import ua.org.training.library.model.PlaceName;

import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface PlaceNameDao extends GenericDao<PlaceName> {
    List<PlaceName> getAllByPlaceId(Connection connection, Long id);

    Optional<PlaceName> getByPlaceId(Connection connection, Long placeId, Locale locale);
}

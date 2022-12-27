package ua.org.training.library.dao.collector;

import ua.org.training.library.model.Place;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaceCollector implements ObjectCollector<Place> {
    @Override
    public Place collectFromResultSet(ResultSet rs) throws SQLException {
        Place place = new Place();
        place.setId(rs.getLong("id"));
        place.setName(rs.getString("name"));
        return place;
    }
}

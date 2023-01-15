package ua.org.training.library.dao.collector;

import ua.org.training.library.model.Place;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaceCollector implements ObjectCollector<Place> {
    @Override
    public Place collectFromResultSet(ResultSet rs) throws SQLException {
        return Place.builder()
                .setId(rs.getLong("id"))
                .setName(rs.getString("name"))
                .createPlace();
    }
}

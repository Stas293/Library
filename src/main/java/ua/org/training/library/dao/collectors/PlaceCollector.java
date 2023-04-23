package ua.org.training.library.dao.collectors;


import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.model.Place;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("placeCollector")
public class PlaceCollector implements Collector<Place> {
    @Override
    public Place collect(ResultSet rs) throws SQLException {
        return Place.builder()
                .id(rs.getLong(1))
                .code(rs.getString(2))
                .defaultDays(rs.getInt(3))
                .choosable(rs.getBoolean(4))
                .build();
    }
}

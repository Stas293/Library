package ua.org.training.library.dao;

import ua.org.training.library.model.Place;
import ua.org.training.library.utility.page.Page;

import java.util.List;
import java.util.Optional;

public interface PlaceDao extends GenericDao<Place> {
    Optional<Place> getByOrderId(Long id);

    Optional<Place> getByName(String name);

    List<Place> getAll();
}

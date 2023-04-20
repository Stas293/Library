package ua.org.training.library.repository;


import ua.org.training.library.model.Place;
import ua.org.training.library.repository.base.JRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface PlaceRepository extends JRepository<Place, Long> {
    Optional<Place> getByCode(String name);

    List<Place> findAll(Locale locale);
}

package ua.org.training.library.service;


import ua.org.training.library.dto.PlaceDto;
import ua.org.training.library.model.Place;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface PlaceService extends GenericService<Long, Place> {
    Optional<Place> getByName(String name);

    List<PlaceDto> getAllPlaces(Locale locale);
}

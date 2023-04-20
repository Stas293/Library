package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.dto.PlaceDto;
import ua.org.training.library.model.Place;
import ua.org.training.library.repository.PlaceRepository;
import ua.org.training.library.service.PlaceService;
import ua.org.training.library.utility.mapper.ObjectMapper;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<Place> getByName(String name) {
        log.info("Getting place by name: {}", name);
        return placeRepository.getByCode(name);
    }

    @Override
    public List<PlaceDto> getAllPlaces(Locale locale) {
        log.info("Getting all places by locale: {}", locale);
        List<Place> places = placeRepository.findAll(locale);
        return objectMapper.mapPlaceListToPlaceDtoList(places);
    }
}

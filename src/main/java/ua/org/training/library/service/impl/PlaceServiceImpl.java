package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.dto.PlaceDto;
import ua.org.training.library.model.Place;
import ua.org.training.library.repository.PlaceRepository;
import ua.org.training.library.service.PlaceService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;

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
    @Transactional
    public Place createModel(Place model) {
        log.info("Creating place: {}", model);
        return placeRepository.save(model);
    }

    @Override
    @Transactional
    public void updateModel(Place model) {
        log.info("Updating place: {}", model);
        placeRepository.save(model);
    }

    @Override
    @Transactional
    public void deleteModel(Place author) {
        log.info("Deleting place: {}", author);
        placeRepository.delete(author);
    }

    @Override
    @Transactional
    public void createModels(List<Place> models) {
        log.info("Creating places: {}", models);
        placeRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void updateModels(List<Place> models) {
        log.info("Updating places: {}", models);
        placeRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void deleteModels(List<Place> models) {
        log.info("Deleting places: {}", models);
        placeRepository.deleteAll(models);
    }

    @Override
    public List<Place> getAllModels() {
        log.info("Getting all places");
        return placeRepository.findAll();
    }

    @Override
    public List<Place> getModelsByIds(List<Long> ids) {
        log.info("Getting places by ids: {}", ids);
        return placeRepository.findAllById(ids);
    }

    @Override
    public long countModels() {
        log.info("Counting places");
        return placeRepository.count();
    }

    @Override
    public void deleteAllModels() {
        log.info("Deleting all places");
        placeRepository.deleteAll();
    }

    @Override
    public boolean checkIfExists(Place model) {
        log.info("Checking if place exists: {}", model);
        return placeRepository.existsById(model.getId());
    }

    @Override
    public Page<Place> getModelsByPage(int pageNumber, int pageSize) {
        log.info("Getting places by page: {}, {}", pageNumber, pageSize);
        return placeRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    @Transactional
    public void deleteModelById(Long id) {
        log.info("Deleting place by id: {}", id);
        placeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteModelsByIds(List<Long> ids) {
        log.info("Deleting places by ids: {}", ids);
        placeRepository.deleteAllById(ids);
    }

    @Override
    public List<Place> getAllModels(String sortField, String sortOrder) {
        log.info("Getting all places by sort: {}, {}", sortField, sortOrder);
        return placeRepository.findAll(Sort.by(Sort.Direction.fromString(sortOrder), sortField));
    }

    @Override
    public Page<Place> getModelsByPage(int pageNumber, int pageSize, Sort.Direction direction, String... sortField) {
        log.info("Getting places by page: {}, {}, {}, {}", pageNumber, pageSize, direction, sortField);
        return placeRepository.findAll(PageRequest.of(pageNumber, pageSize, direction, sortField));
    }

    @Override
    public Optional<Place> getByName(String name) {
        log.info("Getting place by name: {}", name);
        return placeRepository.getByName(name);
    }

    @Override
    public List<PlaceDto> getAllPlaces(Locale locale) {
        log.info("Getting all places by locale: {}", locale);
        List<Place> places = placeRepository.findAll(locale);
        return objectMapper.mapPlaceListToPlaceDtoList(places);
    }
}

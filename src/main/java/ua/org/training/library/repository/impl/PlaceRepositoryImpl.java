package ua.org.training.library.repository.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.connections.TransactionManager;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dao.PlaceDao;
import ua.org.training.library.dao.PlaceNameDao;
import ua.org.training.library.model.Place;
import ua.org.training.library.model.PlaceName;
import ua.org.training.library.repository.PlaceRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PlaceRepositoryImpl implements PlaceRepository {
    private final TransactionManager transactionManager;
    private final PlaceDao placeDao;
    private final PlaceNameDao placeNameDao;

    @Override
    public Optional<Place> getByName(String name) {
        log.info("Getting place by name: {}", name);
        Optional<Place> place = placeDao.getByCode(transactionManager.getConnection(), name);
        place.ifPresent(
                model -> model.setNames(
                        placeNameDao.getAllByPlaceId(transactionManager.getConnection(), model.getId())
                )
        );
        return place;
    }

    @Override
    public List<Place> findAll(Locale locale) {
        log.info("Getting all places");
        List<Place> places = placeDao.getAll(transactionManager.getConnection());
        places.forEach(
                model -> model.setNames(
                        List.of(
                                placeNameDao.getByPlaceId(transactionManager.getConnection(), model.getId(), locale).orElseThrow()
                        )
                )
        );
        return places;
    }

    @Override
    public Place save(Place entity) {
        log.info("Saving place: {}", entity);
        if (entity.getId() == null) {
            log.info("Creating place: {}", entity);
            Place savedEntity = placeDao.create(transactionManager.getConnection(), entity);
            entity.setId(savedEntity.getId());
            entity.getNames().forEach(placeName -> placeName.setPlace(savedEntity));
            List<PlaceName> placeNames = placeNameDao.create(transactionManager.getConnection(), entity.getNames());
            entity.setNames(placeNames);
        } else {
            log.info("Updating place: {}", entity);
            placeDao.update(transactionManager.getConnection(), entity);
            placeNameDao.update(transactionManager.getConnection(), entity.getNames());
        }
        return entity;
    }

    @Override
    public List<Place> saveAll(List<Place> entities) {
        log.info("Saving places: {}", entities);
        List<Place> placesToSave = entities.stream()
                .filter(place -> place.getId() == null)
                .collect(Collectors.toList());
        List<Place> placesToUpdate = entities.stream()
                .filter(place -> place.getId() != null)
                .toList();
        if (!placesToSave.isEmpty()) {
            log.info("Creating places: {}", placesToSave);
            List<Place> savedEntities = placeDao.create(transactionManager.getConnection(), placesToSave);
            for (int i = 0; i < placesToSave.size(); i++) {
                Place place = placesToSave.get(i);
                Place savedPlace = savedEntities.get(i);
                place.setId(savedPlace.getId());
                List<PlaceName> placeNames = place.getNames();
                placeNames.forEach(placeName -> placeName.setPlace(savedPlace));
                place.setNames(placeNameDao.create(transactionManager.getConnection(), placeNames));
            }
        }
        if (!placesToUpdate.isEmpty()) {
            log.info("Updating places: {}", placesToUpdate);
            placeDao.update(transactionManager.getConnection(), placesToUpdate);
            placesToUpdate.forEach(place -> placeNameDao.update(transactionManager.getConnection(), place.getNames()));
        }
        placesToSave.addAll(placesToUpdate);
        return placesToSave;
    }

    @Override
    public Optional<Place> findById(Long aLong) {
        log.info("Getting place by id: {}", aLong);
        Optional<Place> place = placeDao.getById(transactionManager.getConnection(), aLong);
        place.ifPresent(model -> model.setNames(
                placeNameDao.getAllByPlaceId(transactionManager.getConnection(), model.getId()))
        );
        return place;
    }

    @Override
    public boolean existsById(Long aLong) {
        log.info("Checking if place exists by id: {}", aLong);
        return placeDao.getById(transactionManager.getConnection(), aLong).isPresent();
    }

    @Override
    public List<Place> findAll() {
        log.info("Getting all places");
        List<Place> places = placeDao.getAll(transactionManager.getConnection());
        places.forEach(place -> place.setNames(
                placeNameDao.getAllByPlaceId(transactionManager.getConnection(), place.getId()))
        );
        return places;
    }

    @Override
    public List<Place> findAllById(List<Long> longs) {
        log.info("Getting all places by ids: {}", longs);
        List<Place> places = placeDao.getByIds(transactionManager.getConnection(), longs);
        places.forEach(place -> place.setNames(
                placeNameDao.getAllByPlaceId(transactionManager.getConnection(), place.getId()))
        );
        return places;
    }

    @Override
    public long count() {
        log.info("Counting all places");
        return placeDao.count(transactionManager.getConnection());
    }

    @Override
    public void deleteById(Long aLong) {
        log.info("Deleting place by id: {}", aLong);
        placeDao.delete(transactionManager.getConnection(), aLong);
    }

    @Override
    public void delete(Place entity) {
        log.info("Deleting place: {}", entity);
        placeDao.delete(transactionManager.getConnection(), entity.getId());
    }

    @Override
    public void deleteAllById(List<? extends Long> ids) {
        log.info("Deleting all places by ids: {}", ids);
        placeDao.deleteAll(transactionManager.getConnection(), ids);
    }

    @Override
    public void deleteAll(List<? extends Place> entities) {
        log.info("Deleting all places: {}", entities);
        List<Long> ids = entities.stream()
                .map(Place::getId)
                .toList();
        placeDao.deleteAll(transactionManager.getConnection(), ids);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all places");
        placeDao.deleteAll(transactionManager.getConnection());
    }

    @Override
    public List<Place> findAll(Sort sort) {
        log.info("Getting all places by sort: {}", sort);
        List<Place> places = placeDao.getAll(transactionManager.getConnection(), sort);
        places.forEach(place -> place.setNames(
                placeNameDao.getAllByPlaceId(transactionManager.getConnection(), place.getId()))
        );
        return places;
    }

    @Override
    public Page<Place> findAll(Pageable pageable) {
        log.info("Getting all places by pageable: {}", pageable);
        Page<Place> placePage = placeDao.getPage(transactionManager.getConnection(), pageable);
        placePage.getContent().forEach(place -> place.setNames(
                placeNameDao.getAllByPlaceId(transactionManager.getConnection(), place.getId()))
        );
        return placePage;
    }
}

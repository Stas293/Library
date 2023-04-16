package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.model.Keyword;
import ua.org.training.library.repository.KeywordRepository;
import ua.org.training.library.service.KeywordService;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KeywordServiceImpl implements KeywordService {
    private final KeywordRepository repository;

    @Transactional
    @Override
    public Keyword createModel(Keyword model) {
        log.info("Creating keyword: {}", model);
        return repository.save(model);
    }

    @Transactional
    @Override
    public void updateModel(Keyword model) {
        log.info("Updating keyword: {}", model);
        repository.save(model);
    }

    @Transactional
    @Override
    public void deleteModel(Keyword author) {
        log.info("Deleting keyword: {}", author);
        repository.delete(author);
    }

    @Transactional
    @Override
    public void createModels(List<Keyword> models) {
        log.info("Creating keywords: {}", models);
        repository.saveAll(models);
    }

    @Transactional
    @Override
    public void updateModels(List<Keyword> models) {
        log.info("Updating keywords: {}", models);
        repository.saveAll(models);
    }

    @Transactional
    @Override
    public void deleteModels(List<Keyword> models) {
        log.info("Deleting keywords: {}", models);
        repository.deleteAll(models);
    }

    @Override
    public List<Keyword> getAllModels() {
        log.info("Getting all keywords");
        return repository.findAll();
    }

    @Override
    public List<Keyword> getModelsByIds(List<Long> ids) {
        log.info("Getting keywords by ids: {}", ids);
        return repository.findAllById(ids);
    }

    @Override
    public long countModels() {
        log.info("Counting keywords");
        return repository.count();
    }

    @Override
    public void deleteAllModels() {
        log.info("Deleting all keywords");
        repository.deleteAll();
    }

    @Override
    public boolean checkIfExists(Keyword model) {
        log.info("Checking if keyword exists: {}", model);
        return repository.existsById(model.getId());
    }

    @Override
    public Page<Keyword> getModelsByPage(int pageNumber, int pageSize) {
        log.info("Getting keywords by page: {}, {}", pageNumber, pageSize);
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    @Transactional
    public void deleteModelById(Long id) {
        log.info("Deleting keyword by id: {}", id);
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteModelsByIds(List<Long> ids) {
        log.info("Deleting keywords by ids: {}", ids);
        repository.deleteAllById(ids);
    }

    @Override
    public List<Keyword> getAllModels(String sortField, String sortOrder) {
        log.info("Getting all keywords by sort: {}, {}", sortField, sortOrder);
        return repository.findAll(Sort.by(Sort.Direction.fromString(sortOrder), sortField));
    }

    @Override
    public Page<Keyword> getModelsByPage(int pageNumber, int pageSize, Sort.Direction direction, String... sortField) {
        log.info("Getting keywords by page: {}, {}, {}, {}", pageNumber, pageSize, direction, sortField);
        return repository.findAll(PageRequest.of(pageNumber, pageSize, direction, sortField));
    }
}

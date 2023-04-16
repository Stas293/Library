package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.model.Author;
import ua.org.training.library.repository.AuthorRepository;
import ua.org.training.library.service.AuthorService;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;

@Component
@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;

    @Override
    @Transactional
    public Author createModel(Author author) {
        log.info("Creating author: {}", author);
        return repository.save(author);
    }


    @Override
    @Transactional
    public void updateModel(Author author) {
        log.info("Updating author: {}", author);
        repository.save(author);
    }


    @Override
    @Transactional
    public void deleteModel(Author author) {
        log.info("Deleting author: {}", author);
        repository.delete(author);
    }


    @Override
    @Transactional
    public void createModels(List<Author> authors) {
        log.info("Creating authors: {}", authors);
        repository.saveAll(authors);
    }


    @Override
    @Transactional
    public void updateModels(List<Author> authors) {
        log.info("Updating authors: {}", authors);
        repository.saveAll(authors);
    }


    @Override
    @Transactional
    public void deleteModels(List<Author> authors) {
        log.info("Deleting authors: {}", authors);
        repository.deleteAll(authors);
    }


    @Override
    public List<Author> getAllModels() {
        log.info("Getting all authors");
        return repository.findAll();
    }


    @Override
    public List<Author> getModelsByIds(List<Long> ids) {
        log.info("Getting authors by ids: {}", ids);
        return repository.findAllById(ids);
    }


    @Override
    public long countModels() {
        log.info("Counting authors");
        return repository.count();
    }


    @Override
    public void deleteAllModels() {
        log.info("Deleting all authors");
        repository.deleteAll();
    }


    @Override
    public boolean checkIfExists(Author author) {
        log.info("Checking if author exists: {}", author);
        return repository.existsById(author.getId());
    }


    @Override
    public Page<Author> getModelsByPage(int pageNumber, int pageSize) {
        log.info("Getting authors by page: {}, {}", pageNumber, pageSize);
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }


    @Override
    @Transactional
    public void deleteModelById(Long id) {
        log.info("Deleting author by id: {}", id);
        repository.deleteById(id);
    }


    @Override
    @Transactional
    public void deleteModelsByIds(List<Long> ids) {
        log.info("Deleting authors by ids: {}", ids);
        repository.deleteAllById(ids);
    }


    @Override
    public List<Author> getAllModels(String sortField, String sortOrder) {
        log.info("Getting all authors by sort: {}, {}", sortField, sortOrder);
        return repository.findAll(Sort.by(Sort.Direction.fromString(sortOrder), sortField));
    }


    @Override
    public Page<Author> getModelsByPage(int pageNumber, int pageSize, Sort.Direction direction, String... sortField) {
        log.info("Getting authors by page: {}, {}, {}, {}", pageNumber, pageSize, direction, sortField);
        return repository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField)));
    }
}

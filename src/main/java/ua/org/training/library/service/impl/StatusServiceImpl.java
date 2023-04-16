package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.dto.StatusDto;
import ua.org.training.library.model.Status;
import ua.org.training.library.repository.StatusRepository;
import ua.org.training.library.service.StatusService;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.mapper.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public Status createModel(Status model) {
        log.info("Creating status: {}", model);
        return statusRepository.save(model);
    }

    @Override
    @Transactional
    public void updateModel(Status model) {
        log.info("Updating status: {}", model);
        statusRepository.save(model);
    }

    @Override
    @Transactional
    public void deleteModel(Status author) {
        log.info("Deleting status: {}", author);
        statusRepository.delete(author);
    }

    @Override
    @Transactional
    public void createModels(List<Status> models) {
        log.info("Creating statuses: {}", models);
        statusRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void updateModels(List<Status> models) {
        log.info("Updating statuses: {}", models);
        statusRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void deleteModels(List<Status> models) {
        log.info("Deleting statuses: {}", models);
        statusRepository.deleteAll(models);
    }

    @Override
    public List<Status> getAllModels() {
        log.info("Getting all statuses");
        return statusRepository.findAll();
    }

    @Override
    public List<Status> getModelsByIds(List<Long> ids) {
        log.info("Getting statuses by ids: {}", ids);
        return statusRepository.findAllById(ids);
    }

    @Override
    public long countModels() {
        log.info("Counting statuses");
        return statusRepository.count();
    }

    @Override
    public void deleteAllModels() {
        log.info("Deleting all statuses");
        statusRepository.deleteAll();
    }

    @Override
    public boolean checkIfExists(Status model) {
        log.info("Checking if status exists: {}", model);
        return statusRepository.existsById(model.getId());
    }

    @Override
    public Page<Status> getModelsByPage(int pageNumber, int pageSize) {
        log.info("Getting statuses by page: {}, {}", pageNumber, pageSize);
        return statusRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    @Transactional
    public void deleteModelById(Long id) {
        log.info("Deleting status by id: {}", id);
        statusRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteModelsByIds(List<Long> ids) {
        log.info("Deleting statuses by ids: {}", ids);
        statusRepository.deleteAllById(ids);
    }

    @Override
    public List<Status> getAllModels(String sortField, String sortOrder) {
        log.info("Getting all statuses by sort: {}, {}", sortField, sortOrder);
        return statusRepository.findAll(Sort.by(Sort.Direction.fromString(sortOrder), sortField));
    }

    @Override
    public Page<Status> getModelsByPage(int pageNumber, int pageSize, Sort.Direction direction, String... sortField) {
        log.info("Getting statuses by page: {}, {}, {}, {}", pageNumber, pageSize, direction, sortField);
        return statusRepository.findAll(PageRequest.of(pageNumber, pageSize, direction, sortField));
    }

    @Override
    public Optional<Status> getByCode(String code) {
        log.info("Getting status by code: {}", code);
        return statusRepository.findByCode(code);
    }

    @Override
    public List<Status> getNextStatuses(Long id) {
        log.info("Getting next statuses by id: {}", id);
        return statusRepository.getNextStatusesForStatusById(id);
    }

    @Override
    public List<StatusDto> getNextStatusesByOrderId(long id) {
        log.info("Getting statuses by order id: {}", id);
        Optional<Status> statusByOrderId = statusRepository.getStatusByOrderId(id);
        return statusByOrderId.map(status -> statusRepository.getNextStatusesForStatusById(status.getId())
                .parallelStream()
                .map(mapper::mapStatusToStatusDto)
                .collect(Collectors.toList()))
                .orElseGet(List::of);
    }
}

package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.HistoryOrderRepository;
import ua.org.training.library.service.HistoryOrderService;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HistoryOrderServiceImpl implements HistoryOrderService {
    private final HistoryOrderRepository historyOrderRepository;

    @Override
    @Transactional
    public HistoryOrder createModel(HistoryOrder model) {
        log.info("Creating history order: {}", model);
        return historyOrderRepository.save(model);
    }

    @Override
    @Transactional
    public void updateModel(HistoryOrder model) {
        log.info("Updating history order: {}", model);
        historyOrderRepository.save(model);
    }

    @Override
    @Transactional
    public void deleteModel(HistoryOrder author) {
        log.info("Deleting history order: {}", author);
        historyOrderRepository.delete(author);
    }

    @Override
    @Transactional
    public void createModels(List<HistoryOrder> models) {
        log.info("Creating history orders: {}", models);
        historyOrderRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void updateModels(List<HistoryOrder> models) {
        log.info("Updating history orders: {}", models);
        historyOrderRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void deleteModels(List<HistoryOrder> models) {
        log.info("Deleting history orders: {}", models);
        historyOrderRepository.deleteAll(models);
    }

    @Override
    public List<HistoryOrder> getAllModels() {
        log.info("Getting all history orders");
        return historyOrderRepository.findAll();
    }

    @Override
    public List<HistoryOrder> getModelsByIds(List<Long> ids) {
        log.info("Getting history orders by ids: {}", ids);
        return historyOrderRepository.findAllById(ids);
    }

    @Override
    public long countModels() {
        log.info("Counting history orders");
        return historyOrderRepository.count();
    }

    @Override
    public void deleteAllModels() {
        log.info("Deleting all history orders");
        historyOrderRepository.deleteAll();
    }

    @Override
    public boolean checkIfExists(HistoryOrder model) {
        log.info("Checking if history order exists: {}", model);
        return historyOrderRepository.existsById(model.getId());
    }

    @Override
    public Page<HistoryOrder> getModelsByPage(int pageNumber, int pageSize) {
        log.info("Getting history orders by page: {}, {}", pageNumber, pageSize);
        return historyOrderRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    @Transactional
    public void deleteModelById(Long id) {
        log.info("Deleting history order by id: {}", id);
        historyOrderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteModelsByIds(List<Long> ids) {
        log.info("Deleting history orders by ids: {}", ids);
        historyOrderRepository.deleteAllById(ids);
    }

    @Override
    public List<HistoryOrder> getAllModels(String sortField, String sortOrder) {
        log.info("Getting all history orders by sort: {}, {}", sortField, sortOrder);
        return historyOrderRepository.findAll(Sort.by(Sort.Direction.fromString(sortOrder), sortField));
    }

    @Override
    public Page<HistoryOrder> getModelsByPage(int pageNumber, int pageSize, Sort.Direction direction, String... sortField) {
        log.info("Getting history orders by page: {}, {}, {}, {}", pageNumber, pageSize, direction, sortField);
        return historyOrderRepository.findAll(PageRequest.of(pageNumber, pageSize, direction, sortField));
    }

    @Override
    public Page<HistoryOrder> getPageByUser(Pageable page, long userId) {
        log.info("Getting history orders by user: {}, {}", page, userId);
        return historyOrderRepository.getPageByUser(page, User.builder().id(userId).build());
    }
}

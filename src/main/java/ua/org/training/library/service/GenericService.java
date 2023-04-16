package ua.org.training.library.service;


import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;

public interface GenericService<K, V> {
    @Transactional
    V createModel(V model);

    @Transactional
    void updateModel(V model);

    @Transactional
    void deleteModel(V author);

    @Transactional
    void createModels(List<V> models);

    @Transactional
    void updateModels(List<V> models);

    @Transactional
    void deleteModels(List<V> models);

    List<V> getAllModels();

    List<V> getModelsByIds(List<K> ids);

    long countModels();

    void deleteAllModels();

    boolean checkIfExists(V model);

    Page<V> getModelsByPage(int pageNumber, int pageSize);

    @Transactional
    void deleteModelById(K id);

    @Transactional
    void deleteModelsByIds(List<K> ids);

    List<V> getAllModels(String sortField, String sortOrder);

    Page<V> getModelsByPage(int pageNumber, int pageSize, Sort.Direction direction, String... sortField);
}

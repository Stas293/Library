package ua.org.training.library.repository.base;


import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;

public interface ListPagingAndSortingRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
    List<T> findAll(Sort sort);
}
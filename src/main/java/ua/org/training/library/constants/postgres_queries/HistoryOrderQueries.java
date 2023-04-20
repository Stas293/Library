package ua.org.training.library.constants.postgres_queries;


import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

public interface HistoryOrderQueries {
    String getCreateQuery();

    String getSelectByIdQuery();

    String getSelectByIdsQuery(int size);

    String getSelectAllQuery(Sort sort);

    String getSelectAllQuery(Pageable page);
    String getSelectAllQuery();

    String getUpdateQuery();

    String getDeleteQuery();

    String getCountQuery();

    String getDeleteAllQuery();

    String getDeleteAllByIdsQuery(int size);

    String getSelectAllByUserIdQuery(Pageable page);

    String getCountByUserIdQuery();

    String getSelectAllByUserIdAndSearchQuery(Pageable page);

    String getCountByUserIdAndSearchQuery();
}

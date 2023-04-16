package ua.org.training.library.constants.postgres_queries;

import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;

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
}

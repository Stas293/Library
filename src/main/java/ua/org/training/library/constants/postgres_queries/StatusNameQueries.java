package ua.org.training.library.constants.postgres_queries;

import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;

public interface StatusNameQueries {
    String getCreateStatusNameQuery();

    String getGetStatusNameByIdQuery();

    String getGetStatusNameByIdsQuery(int size);

    String getGetAllStatusNamesQuery();

    String getGetAllStatusNamesQuery(Sort sort);

    String getGetPageStatusNamesQuery(Pageable page);

    String getUpdateStatusNameQuery();

    String getDeleteStatusNameByIdQuery();

    String getGetCountStatusNamesQuery();

    String getDeleteAllStatusNamesQuery();

    String getDeleteStatusNamesByIdsQuery(int size);

    String getUpdateStatusNamesQuery();

    String getGetStatusNamesByStatusIdQuery();

    String getDeleteStatusNamesByStatusIdQuery();

    String getGetStatusNameByStatusIdAndLocaleQuery();
}

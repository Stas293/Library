package ua.org.training.library.constants.postgres_queries;

import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;

public interface StatusQueries {
    String getCreateStatusQuery();

    String getGetStatusByIdQuery();

    String getGetStatusesByIdsQuery(int size);

    String getGetAllStatusesQuery();

    String getGetAllStatusesQuery(Sort sort);

    String getGetPageStatusesQuery(Pageable page);

    String getUpdateStatusQuery();

    String getDeleteStatusByIdQuery();

    String getGetCountStatusesQuery();

    String getDeleteAllStatusesQuery();

    String getDeleteStatusesByIdsQuery(int size);

    String getGetStatusByCodeQuery();

    String getGetStatusByOrderIdQuery();

    String getGetStatusByHistoryOrderIdQuery();

    String getGetNextStatusesForStatusByIdQuery();
}

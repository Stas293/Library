package ua.org.training.library.enums.constants;


import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

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

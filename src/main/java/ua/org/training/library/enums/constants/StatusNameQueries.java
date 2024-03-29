package ua.org.training.library.enums.constants;


import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

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

    String getGetStatusNameByStatusIdAndLocaleQuery();
}

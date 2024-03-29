package ua.org.training.library.enums.constants;


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

    String getSelectAllByUserIdAndSearchQuery(Pageable page);
}

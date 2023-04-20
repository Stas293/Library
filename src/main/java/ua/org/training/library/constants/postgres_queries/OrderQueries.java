package ua.org.training.library.constants.postgres_queries;


import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

public interface OrderQueries {
    String getCreateQuery();

    String getSelectByIdQuery();

    String getSelectByIdsQuery(int size);

    String getSelectAllQuery();

    String getSelectAllQuery(Sort sort);

    String getSelectAllQuery(Pageable page);

    String getUpdateQuery();

    String getDeleteQuery();

    String getCountQuery();

    String getDeleteAllQuery();

    String getDeleteByIdsQuery(int size);

    String getSelectAllByBookIdQuery(Pageable page);

    String getCountByBookIdQuery();

    String getSelectAllByStatusAndUserIdQuery(Pageable page);

    String getCountByStatusAndUserIdQuery();

    String getSelectAllByStatusIdQuery(Pageable page);

    String getCountByStatusIdQuery();

    String getSelectAllByStatusAndUserIdAndSearchQuery(Pageable page);

    String getCountByStatusAndUserIdAndSearchQuery();

    String getSelectAllByStatusAndSearchQuery(Pageable page);

    String getCountByStatusAndSearchQuery();

    String getSelectAllByStatusAndPlaceAndSearchQuery(Pageable page);

    String getCountByStatusAndPlaceAndSearchQuery();

    String getSelectAllByStatusAndPlaceQuery(Pageable page);

    String getCountByStatusAndPlaceQuery();

    String getSelectByUserIdAndBookIdQuery();
}

package ua.org.training.library.enums.constants;


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

    String getSelectAllByStatusAndUserIdQuery(Pageable page);

    String getSelectAllByStatusIdQuery(Pageable page);

    String getSelectAllByStatusAndUserIdAndSearchQuery(Pageable page);

    String getSelectAllByStatusAndSearchQuery(Pageable page);

    String getSelectAllByStatusAndPlaceAndSearchQuery(Pageable page);

    String getSelectAllByStatusAndPlaceQuery(Pageable page);

    String getSelectByUserIdAndBookIdQuery();

    String getSelectByBookIdQuery();
}

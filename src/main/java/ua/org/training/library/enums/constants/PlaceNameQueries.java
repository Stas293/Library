package ua.org.training.library.enums.constants;


import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.List;

public interface PlaceNameQueries {
    String getCreateQuery();

    String getGetByIdQuery();

    String getGetByIdsQuery(int size);

    String getGetAllQuery();

    String getGetAllQuery(Sort sort);

    String getGetPageQuery(Pageable page);

    String getUpdateQuery();

    String getDeleteQuery();

    String getCountQuery();

    String getDeleteAllQuery();

    String getDeleteAllQuery(List<? extends Long> longs);

    String getGetAllByPlaceIdQuery();

    String getGetByPlaceIdAndLocaleQuery();
}

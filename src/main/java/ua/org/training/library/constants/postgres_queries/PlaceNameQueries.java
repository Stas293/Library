package ua.org.training.library.constants.postgres_queries;

import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;

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

package ua.org.training.library.constants.postgres_queries;

import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;

public interface RoleQueries {
    String getCreateQuery();

    String getGetByIdQuery();

    String getGetByIdsQuery(int size);

    String getGetAllQuery();

    String getGetAllQuery(Sort sort);

    String getGetPageQuery(Pageable page);

    String getUpdateQuery();

    String getDeleteQuery();

    String getGetCountQuery();

    String getDeleteAllQuery();

    String getDeleteAllQuery(int size);

    String getGetByCodeQuery();

    String getGetByNameQuery();

    String getGetRolesByUserIdQuery();


}

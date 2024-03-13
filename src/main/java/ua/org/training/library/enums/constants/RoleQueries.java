package ua.org.training.library.enums.constants;

import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

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

    String getGetAllByCodesQuery(int size);
}

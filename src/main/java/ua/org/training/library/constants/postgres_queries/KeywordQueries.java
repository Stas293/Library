package ua.org.training.library.constants.postgres_queries;

import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;

public interface KeywordQueries {
    String getInsertQuery();

    String getSelectByIdQuery();

    String getSelectByIdsQuery(int size);

    String getSelectAllQuery();

    String getSelectAllQuery(Sort sort);

    String getSelectAllQuery(Pageable page);

    String getUpdateQuery();

    String getDeleteByIdQuery();

    String getCountQuery();

    String getDeleteAllQuery();

    String getDeleteByIdsQuery(int size);

    String getSelectByBookIdQuery();

    String getDeleteByBookIdQuery();

    String getInserKeywordToBookQuery();
}

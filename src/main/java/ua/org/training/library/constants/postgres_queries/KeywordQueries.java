package ua.org.training.library.constants.postgres_queries;


import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

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

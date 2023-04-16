package ua.org.training.library.constants.postgres_queries;

import com.project.university.system_library.utility.page.impl.Sort;

public interface AuthorQueries {
    String getAuthorsByBookId();

    String getCreateAuthor();

    String getAuthorById();

    String getAllAuthors();

    String getPageAuthors(Sort sort);

    String getUpdateAuthor();

    String getDeleteAuthor();

    String getAuthorsByIds(int size);

    String getCountAuthors();

    String getDeleteAllAuthors();

    String getAllAuthors(Sort sort);

    String getDeleteAuthorsByIds(int size);

    String getDeleteAuthorsByBookId();

    String getSaveAuthorsToBook();
}

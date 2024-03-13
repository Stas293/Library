package ua.org.training.library.enums.constants;


import ua.org.training.library.utility.page.impl.Sort;

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

    String getSearchAuthors(Sort sort);

    String getAuthorsCount();

    String getFindAllByNameContainingIgnoreCase();
}

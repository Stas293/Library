package ua.org.training.library.constants.postgres_queries;

import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.Sort;

public interface BookQueries {
    String getBooksByAuthorIdQuery(Pageable page);

    String getBooksByLanguageQuery(Pageable page);

    String getBookByOrderIdQuery();

    String getBooksWhichUserDidNotOrderQuery(Pageable page);

    String getBookByISBNQuery();

    String getCreateBookQuery();

    String getGetBookByIdQuery();

    String getGetBooksByIdsQuery(int size);

    String getGetAllBooksQuery();

    String getGetPageOfBooksQuery(Pageable page);

    String getUpdateBookQuery();

    String getDeleteBookByIdQuery();

    String getGetCountOfBooksQuery();

    String getDeleteAllBooksQuery();

    String getDeleteBooksByIdsQuery(int size);

    String getSearchBooksQuery(Pageable page);

    String getSearchBooksWhichUserDidNotOrderQuery(Pageable page);

    String getGetAllBooksQuery(Sort sort);

    String getCountSearchBooksQuery();

    String getCountBooksWhichUserDidNotOrderQuery();
}

package ua.org.training.library.dao;


import ua.org.training.library.model.Author;

import java.sql.Connection;
import java.util.List;

public interface AuthorDao extends GenericDao<Author> {
    List<Author> getAuthorsByBookId(Connection connection,
                                    Long id);

    void setAuthorsToBook(Connection connection,
                          Long bookId,
                          List<Author> authors);
}

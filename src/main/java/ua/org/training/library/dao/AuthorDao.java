package ua.org.training.library.dao;

import ua.org.training.library.model.Author;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.sql.Connection;
import java.util.List;

public interface AuthorDao extends GenericDao<Author> {
    List<Author> getAuthorsByBookId(Connection connection,
                                    Long id);

    void setAuthorsToBook(Connection connection,
                          Long bookId,
                          List<Author> authors);

    Page<Author> searchAuthors(Connection conn, Pageable page, String search);

    List<Author> findAllByNameContainingIgnoreCase(Connection conn, String search);
}

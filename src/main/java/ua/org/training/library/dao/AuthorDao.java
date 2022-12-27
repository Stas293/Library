package ua.org.training.library.dao;

import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;

import java.sql.SQLException;
import java.util.List;

public interface AuthorDao extends GenericDao<Author> {
    List<Author> getAuthorsByBookId(Long id);

    List<Author> getAuthorsByBookIsbn(String isbn);
}

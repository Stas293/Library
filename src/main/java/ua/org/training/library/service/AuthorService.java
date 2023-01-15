package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;

import java.sql.SQLException;
import java.util.List;

public class AuthorService {
    private static final Logger LOGGER = LogManager.getLogger(AuthorService.class);
    private final DaoFactory daoFactory;

    public AuthorService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public List<Author> getAllAuthors(String isbn) throws ServiceException, ConnectionDBException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            return authorDao.getAuthorsByBookIsbn(isbn);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting all authors", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting all authors", e);
            throw new ServiceException("Error while getting all authors", e);
        }
    }

    public long createAuthor(Author author) throws ServiceException, ConnectionDBException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            return authorDao.create(author);
        } catch (SQLException | JDBCException e) {
            LOGGER.error("Error while creating author", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while creating author", e);
            throw new ServiceException("Error while creating author", e);
        }
    }

    public Author getAuthorById(long id) throws ServiceException, ConnectionDBException {
        Author author;
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            author = authorDao.getById(id).orElseThrow(() -> new ServiceException("Author not found"));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting author by id", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting author by id", e);
            throw new ServiceException("Error while getting author by id", e);
        }
        return author;
    }

    public String getAuthorPage(Page<Author> authorPage) throws ServiceException, ConnectionDBException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            PageService<Author> pageService = new PageService<>();
            return pageService.jsonifyPage(authorDao.getPage(authorPage));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting author page", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting author page", e);
            throw new ServiceException("Error while getting author page", e);
        }
    }

    public void updateAuthor(Author author) throws ServiceException, ConnectionDBException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            authorDao.update(author);
        } catch (SQLException | JDBCException e) {
            LOGGER.error("Error while updating author", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while updating author", e);
            throw new ServiceException("Error while updating author", e);
        }
    }

    public void deleteAuthor(long id) throws ServiceException, ConnectionDBException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            authorDao.delete(id);
        } catch (SQLException | JDBCException e) {
            LOGGER.error("Error while deleting author", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while deleting author", e);
            throw new ServiceException("Error while deleting author", e);
        }
    }
}

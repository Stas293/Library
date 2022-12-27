package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.dao.DaoFactory;
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
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public AuthorService() {
    }

    public AuthorService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public List<Author> getAllAuthors(String isbn) throws ServiceException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            return authorDao.getAuthorsByBookIsbn(isbn);
        } catch (JDBCException e) {
            LOGGER.error("Error while getting all authors", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting all authors", e);
            throw new ServiceException("Error while getting all authors", e);
        }
        return List.of();
    }

    public long createAuthor(Author author) throws ServiceException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            return authorDao.create(author);
        } catch (SQLException e) {
            LOGGER.error("Error while creating author", e);
            throw new ServiceException("Error while creating author", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while creating author", e);
        } catch (DaoException e) {
            LOGGER.error("Error while creating author", e);
            throw new ServiceException("Error while creating author", e);
        }
        return 0;
    }

    public Author getAuthorById(long id) throws ServiceException {
        Author author = null;
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            author = authorDao.getById(id).orElseThrow(() -> new ServiceException("Author not found"));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting author by id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting author by id", e);
            throw new ServiceException("Error while getting author by id", e);
        }
        return author;
    }

    public String getAuthorPage(Page<Author> authorPage) throws ServiceException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            PageService<Author> pageService = new PageService<>();
            return pageService.jsonifyPage(authorDao.getPage(authorPage));
        } catch (JDBCException e) {
            LOGGER.error("Error while getting author page", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting author page", e);
            throw new ServiceException("Error while getting author page", e);
        }
        return null;
    }

    public void updateAuthor(Author author) throws ServiceException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            authorDao.update(author);
        } catch (SQLException e) {
            LOGGER.error("Error while updating author", e);
            throw new ServiceException("Error while updating author", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while updating author", e);
        } catch (DaoException e) {
            LOGGER.error("Error while updating author", e);
            throw new ServiceException("Error while updating author", e);
        }
    }

    public void deleteAuthor(long id) throws ServiceException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            authorDao.delete(id);
        } catch (SQLException e) {
            LOGGER.error("Error while deleting author", e);
            throw new ServiceException("Error while deleting author", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while deleting author", e);
        } catch (DaoException e) {
            LOGGER.error("Error while deleting author", e);
            throw new ServiceException("Error while deleting author", e);
        }
    }
}

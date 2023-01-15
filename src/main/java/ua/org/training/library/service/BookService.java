package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.dao.BookDao;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dto.BookDTO;
import ua.org.training.library.exceptions.*;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.DTOMapper;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class BookService {
    private static final Logger LOGGER = LogManager.getLogger(BookService.class);
    private final DaoFactory daoFactory;

    public BookService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public String getBooksByAuthorId(Locale locale, Page<Book> authorPage, long authorId) throws ServiceException, ConnectionDBException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getBooksByAuthorId(
                    authorPage,
                    authorId
            )));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get books by this author", e);
            throw new ServiceException("Can`t get books by this author", e);
        }
    }

    private Page<BookDTO> getDtoPage(Locale locale, Page<Book> bookPage) throws ConnectionDBException {
        try {
            return Page.<BookDTO>builder()
                    .setPageNumber(bookPage.getPageNumber())
                    .setLimit(bookPage.getLimit())
                    .setSorting(bookPage.getSorting())
                    .setSearch(bookPage.getSearch())
                    .setElementsCount(bookPage.getElementsCount())
                    .setData(formatDataBook(locale, bookPage.getData()))
                    .createPage();
        } catch (LoadFieldsException e) {
            LOGGER.error("Can`t load fields", e);
            throw new ConnectionDBException(e.getMessage(), e);
        }
    }

    private List<BookDTO> formatDataBook(Locale locale, List<Book> data) {
        return data.stream().map(book -> {
            try {
                return DTOMapper.bookToDTO(locale, loadFields(book));
            } catch (ConnectionDBException e) {
                throw new LoadFieldsException(e.getMessage(), e);
            }
        }).toList();
    }

    private Book loadFields(Book book) throws ConnectionDBException {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            if (book.getId() == null) {
                book.setAuthors(authorDao.getAuthorsByBookIsbn(book.getISBN()));
            } else {
                book.setAuthors(authorDao.getAuthorsByBookId(book.getId()));
            }
        } catch (JDBCException e) {
            LOGGER.error("Can`t create authorDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        }
        return book;
    }

    public String getBooksByLanguage(Locale locale, Page<Book> bookPage) throws ServiceException, ConnectionDBException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getBooksByLanguage(
                    bookPage,
                    Utility.getLanguage(locale)
            )));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get books by this language", e);
            throw new ServiceException("Can`t get books by this language", e);
        }
    }

    public String getBooksSortedBy(Locale locale, Page<Book> bookPage, String sort) throws ServiceException, ConnectionDBException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getBooksSortedBy(
                    bookPage,
                    sort
            )));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get books by this language", e);
            throw new ServiceException("Can`t get books by this language", e);
        }
    }

    public long createBook(Book book) throws ServiceException, ConnectionDBException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return bookDao.create(book);
        } catch (SQLException | JDBCException e) {
            LOGGER.error("Error while creating book", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while creating book", e);
            throw new ServiceException("Error while creating book", e);
        }
    }

    public void updateBook(Book book) throws ServiceException, ConnectionDBException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            bookDao.update(book);
        } catch (SQLException | JDBCException e) {
            LOGGER.error("Error while updating book", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while updating book", e);
            throw new ServiceException("Error while updating book", e);
        }
    }

    public Book getBookById(long id) throws ServiceException, ConnectionDBException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            Book book = bookDao.getById(id).orElseThrow(() -> new ServiceException("Book not found"));
            try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
                book.setAuthors(authorDao.getAuthorsByBookId(book.getId()));
            }
            return book;
        } catch (JDBCException e) {
            LOGGER.error("Error while getting book by id", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting book by id", e);
            throw new ServiceException("Error while getting book by id", e);
        }
    }

    public String getBookPage(Locale locale, Page<Book> bookPage) throws ServiceException, ConnectionDBException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getPage(bookPage)));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get book page", e);
            throw new ServiceException("Can`t get book page", e);
        }
    }

    public void deleteBook(long id) throws ServiceException, ConnectionDBException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            bookDao.delete(id);
        } catch (SQLException | JDBCException e) {
            LOGGER.error("Error while deleting book", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Error while deleting book", e);
            throw new ServiceException("Error while deleting book", e);
        }
    }

    public String getBookPageWhichUserCanOrder(Locale locale, Page<Book> bookPage, long userId, String orderBy) throws ServiceException, ConnectionDBException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getBooksWhichUserDidNotOrder(bookPage, userId, orderBy)));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
            throw new ConnectionDBException(e.getMessage(), e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get book page", e);
            throw new ServiceException("Can`t get book page", e);
        }
    }
}

package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.AuthorDao;
import ua.org.training.library.dao.BookDao;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dto.BookDTO;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class BookService {
    private static final Logger LOGGER = LogManager.getLogger(BookService.class);
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public BookService() {
    }

    public BookService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public String getBooksByAuthorId(Locale locale, Page<Book> authorPage, long authorId) throws ServiceException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getBooksByAuthorId(
                    authorPage,
                    authorId
            )));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get books by this author", e);
            throw new ServiceException("Can`t get books by this author", e);
        }
        return null;
    }

    private Page<BookDTO> getDtoPage(Locale locale, Page<Book> bookPage) {
        return Page.<BookDTO>builder()
                .setPageNumber(bookPage.getPageNumber())
                .setLimit(bookPage.getLimit())
                .setSorting(bookPage.getSorting())
                .setSearch(bookPage.getSearch())
                .setElementsCount(bookPage.getElementsCount())
                .setData(formatDataBook(locale, bookPage.getData()))
                .createPage();
    }

    private List<BookDTO> formatDataBook(Locale locale, List<Book> data) {
        return data.stream().map(book -> new BookDTO(locale, loadFields(book))).toList();
    }

    private Book loadFields(Book book) {
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            if (book.getId() == null) {
                book.setAuthors(authorDao.getAuthorsByBookIsbn(book.getISBN()));
            } else {
                book.setAuthors(authorDao.getAuthorsByBookId(book.getId()));
            }
        } catch (JDBCException e) {
            LOGGER.error("Can`t create authorDao", e);
        }
        return book;
    }

    public String getBooksByAuthorIdAndLanguage(Locale locale, Page<Book> authorPage, long authorId) throws ServiceException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getBooksByAuthorIdAndLanguage(
                    authorPage,
                    authorId,
                    Utility.getLanguage(locale)
            )));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get books by this author", e);
            throw new ServiceException("Can`t get books by this author", e);
        }
        return null;
    }

    public String getBooksByLanguage(Locale locale, Page<Book> bookPage) throws ServiceException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getBooksByLanguage(
                    bookPage,
                    Utility.getLanguage(locale)
            )));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get books by this language", e);
            throw new ServiceException("Can`t get books by this language", e);
        }
        return null;
    }

    public String getBooksSortedBy(Locale locale, Page<Book> bookPage, String sort) throws ServiceException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getBooksSortedBy(
                    bookPage,
                    sort
            )));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get books by this language", e);
            throw new ServiceException("Can`t get books by this language", e);
        }
        return null;
    }

    public long createBook(Book book) throws ServiceException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return bookDao.create(book);
        } catch (SQLException e) {
            LOGGER.error("Error while creating book", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while creating book", e);
        } catch (DaoException e) {
            LOGGER.error("Error while creating book", e);
            throw new ServiceException("Error while creating book", e);
        }
        return 0;
    }

    public void updateBook(Book book) throws ServiceException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            bookDao.update(book);
        } catch (SQLException e) {
            LOGGER.error("Error while updating book", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while updating book", e);
        } catch (DaoException e) {
            LOGGER.error("Error while updating book", e);
            throw new ServiceException("Error while updating book", e);
        }
    }

    public Book getBookById(long id) throws ServiceException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            Book book = bookDao.getById(id).orElseThrow(() -> new ServiceException("Book not found"));
            try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
                book.setAuthors(authorDao.getAuthorsByBookId(book.getId()));
            }
            return book;
        } catch (JDBCException e) {
            LOGGER.error("Error while getting book by id", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting book by id", e);
            throw new ServiceException("Error while getting book by id", e);
        }
        return null;
    }

    public String getBookPage(Locale locale, Page<Book> bookPage) throws ServiceException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getPage(bookPage)));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get book page", e);
            throw new ServiceException("Can`t get book page", e);
        }
        return null;
    }

    public void deleteBook(long id) throws ServiceException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            bookDao.delete(id);
        } catch (SQLException e) {
            LOGGER.error("Error while deleting book", e);
        } catch (JDBCException e) {
            LOGGER.error("Error while deleting book", e);
        } catch (DaoException e) {
            LOGGER.error("Error while deleting book", e);
            throw new ServiceException("Error while deleting book", e);
        }
    }

    public String getBookPageWhichUserCanOrder(Locale locale, Page<Book> bookPage, long userId, String orderBy) throws ServiceException {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return new PageService<BookDTO>().jsonifyPage(getDtoPage(locale, bookDao.getBooksWhichUserDidNotOrder(bookPage, userId, orderBy)));
        } catch (JDBCException e) {
            LOGGER.error("Can`t create bookDao", e);
        } catch (DaoException e) {
            LOGGER.error("Can`t get book page", e);
            throw new ServiceException("Can`t get book page", e);
        }
        return null;
    }
}

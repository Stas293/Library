package ua.org.training.library.dao;


import ua.org.training.library.model.Book;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface BookDao extends GenericDao<Book> {
    Page<Book> getBooksByAuthorId(Connection connection,
                                  Pageable page, Long authorId);

    Page<Book> getBooksByLanguage(Connection connection,
                                  Pageable page, String language);

    Optional<Book> getBookByOrderId(Connection connection,
                                    Long orderId);

    Page<Book> getBooksWhichUserDidNotOrder(Connection connection,
                                            Pageable page, Long userId);

    Page<Book> searchBooksWhichUserDidNotOrder(Connection connection, Pageable page,
                                               Long userId, String search);

    Optional<Book> getBookByISBN(Connection connection,
                                 String isbn);

    Page<Book> searchBooks(Connection connection,
                           Pageable page, String search);

    long getBookCount(Connection conn, Long id);

    List<Book> getBooksByAuthorId(Connection conn, Long id);
}

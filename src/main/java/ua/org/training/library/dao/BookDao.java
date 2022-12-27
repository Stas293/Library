package ua.org.training.library.dao;

import ua.org.training.library.model.Book;
import ua.org.training.library.utility.page.Page;

import java.util.Optional;

public interface BookDao extends GenericDao<Book> {
    Page<Book> getBooksByAuthorId(
            Page<Book> page,
            Long authorId);

    Page<Book> getBooksByAuthorIdAndLanguage(
            Page<Book> page,
            Long authorId,
            String language);

    Page<Book> getBooksByLanguage(
            Page<Book> page,
            String language);

    Page<Book> getBooksSortedBy(
            Page<Book> page,
            String orderBy);

    Optional<Book> getBookByOrderId(Long orderId);

    Page<Book> getBooksWhichUserDidNotOrder(Page<Book> page, Long userId, String orderBy);
}

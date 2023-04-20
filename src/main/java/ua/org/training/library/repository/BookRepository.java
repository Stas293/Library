package ua.org.training.library.repository;


import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.Order;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.base.JRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface BookRepository extends JRepository<Book, Long> {
    Page<Book> getBooksByAuthor(Pageable page, Author author);

    Page<Book> getBooksByLanguage(Pageable page, Locale language);

    Optional<Book> getBookByOrder(Order order);

    Page<Book> getBooksExceptUserOrders(Pageable page, User user);

    Page<Book> searchBooksExceptUserOrders(Pageable page, User user,
                                           String search);

    Optional<Book> getBookByISBN(String isbn);

    Page<Book> searchBooks(Pageable page, String search);

    long getBookCount(Long id);

    void updateBookCount(Book book);

    List<Book> findAllByAuthors(Author author);
}

package ua.org.training.library.service;


import ua.org.training.library.dto.BookChangeDto;
import ua.org.training.library.dto.BookDto;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.User;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Locale;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> getBookById(long bookId, Locale locale, AuthorityUser authorityUser);

    Page<Book> getBooksByAuthor(Pageable page, Long authorId);

    Page<Book> getBooksByLanguage(Pageable page, Locale language);

    Page<BookDto> searchBooksExceptUserOrders(Pageable page, User user,
                                              String search, Locale locale);
    Optional<Book> getBookByISBN(String isbn);

    Page<BookDto> searchBooks(Pageable page, Locale locale, String search);

    boolean checkBookAvailability(long id);

    Optional<BookDto> getBookById(long id, Locale locale);

    Optional<BookDto> deleteBookById(long id);

    Optional<BookChangeDto> getBookChangeById(long id);

    Optional<BookDto> saveBook(BookChangeDto bookDto);

    Optional<BookDto> deleteBook(long id);
}

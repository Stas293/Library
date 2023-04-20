package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.dto.BookChangeDto;
import ua.org.training.library.dto.BookDto;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.Order;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.*;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.service.BookService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;

import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    @Override
    public Optional<BookDto> getBookById(long id) {
        log.info("Getting book by id: {}", id);
        return bookRepository.findById(id)
                .map(objectMapper::mapBookToBookDto);
    }

    @Override
    public Optional<BookDto> deleteBookById(long id) {
        log.info("Deleting book by id: {}", id);
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return book;
                })
                .map(objectMapper::mapBookToBookDto);
    }

    @Override
    public Optional<BookChangeDto> getBookChangeById(long id) {
        log.info("Getting book change by id: {}", id);
        return bookRepository.findById(id)
                .map(objectMapper::mapBookToBookChangeDto);
    }

    @Override
    @Transactional
    public Optional<BookDto> saveBook(BookChangeDto bookDto) {
        log.info("Saving book: {}", bookDto);
        return Optional.of(bookDto)
                .map(objectMapper::mapBookChangeDtoToBook)
                .map(bookRepository::save)
                .map(book1 -> {
                    book1.setAuthors(null);
                    book1.setKeywords(null);
                    return book1;
                })
                .map(objectMapper::mapBookToBookDto);
    }

    @Override
    public Optional<BookDto> deleteBook(long id) {
        log.info("Deleting book by id: {}", id);
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return book;
                })
                .map(objectMapper::mapBookToBookDto);
    }

    @Override
    public Optional<BookDto> getBookById(long bookId, AuthorityUser authorityUser) {
        log.info("Getting book by bookId: {}", bookId);
        User user = userRepository.getByLogin(authorityUser.getLogin()).orElseThrow();
        Optional<Order> userOrder = orderRepository.findOrderByUserIdAndBookId(user.getId(), bookId);
        if (userOrder.isPresent()) {
            return Optional.empty();
        }
        return bookRepository.findById(bookId)
                .map(objectMapper::mapBookToBookDto);
    }

    @Override
    public Page<Book> getBooksByAuthor(Pageable page, Long authorId) {
        log.info("Getting books by author: {} {}", page, authorId);
        return bookRepository.getBooksByAuthor(page, Author.builder().id(authorId).build());
    }

    @Override
    public Page<Book> getBooksByLanguage(Pageable page, Locale language) {
        log.info("Getting books by language: {} {}", page, language);
        return bookRepository.getBooksByLanguage(page, language);
    }

    @Override
    public Page<BookDto> searchBooksExceptUserOrders(Pageable page, User user, String search) {
        log.info("Searching books except user orders: {} {} {}", page, user, search);
        User userWithOrders = userRepository.getByLogin(user.getLogin()).orElseThrow();
        if (search == null || search.isEmpty()) {
            if (page.getSort() == null || !page.getSort().isOrdered()) {
                Sort sort = Sort.by(Sort.Direction.ASC, "title");
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
            }
            Page<Book> booksExceptUserOrders = bookRepository.getBooksExceptUserOrders(page, userWithOrders);
            return objectMapper.mapBookPageToBookDtoPage(booksExceptUserOrders);
        } else {
            Page<Book> bookPage = bookRepository.searchBooksExceptUserOrders(page, userWithOrders, search);
            return objectMapper.mapBookPageToBookDtoPage(bookPage);
        }
    }

    @Override
    public Optional<Book> getBookByISBN(String isbn) {
        log.info("Getting book by ISBN: {}", isbn);
        return bookRepository.getBookByISBN(isbn);
    }

    @Override
    public Page<BookDto> searchBooks(Pageable page, String search) {
        log.info("Searching books: {} {}", page, search);
        if (search == null || search.isEmpty()) {
            if (page.getSort() == null || !page.getSort().isOrdered()) {
                Sort sort = Sort.by(Sort.Direction.ASC, "title");
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
            }
            Page<Book> bookPage = bookRepository.findAll(page);
            return objectMapper.mapBookPageToBookDtoPage(bookPage);
        } else {
            Page<Book> bookPage = bookRepository.searchBooks(page, search);
            return objectMapper.mapBookPageToBookDtoPage(bookPage);
        }
    }

    @Override
    public boolean checkBookAvailability(long id) {
        log.info("Checking book availability: {}", id);
        Book book = bookRepository.findById(id).orElseThrow();
        return book.getCount() > 0;
    }
}

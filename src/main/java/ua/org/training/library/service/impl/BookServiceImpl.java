package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.dto.AuthorManagementDto;
import ua.org.training.library.dto.BookChangeDto;
import ua.org.training.library.dto.BookDto;
import ua.org.training.library.dto.KeywordManagementDto;
import ua.org.training.library.enums.Validation;
import ua.org.training.library.form.BookChangeFormValidationError;
import ua.org.training.library.model.*;
import ua.org.training.library.repository.*;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.service.BookService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.validator.BookChangeValidator;

import java.util.List;
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
    private final BookChangeValidator bookChangeValidator;

    @Override
    public Optional<BookDto> getBookById(long id, Locale locale) {
        log.info("Getting book by id: {}", id);
        return bookRepository.findById(id)
                .map(book1 -> objectMapper.mapBookToBookDto(book1, locale));
    }

    @Override
    public Optional<BookDto> deleteBookById(long id) {
        log.info("Deleting book by id: {}", id);
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return book;
                })
                .map(book -> objectMapper.mapBookToBookDto(book, Locale.getDefault()));
    }

    @Override
    public Optional<BookChangeDto> getBookChangeById(Locale locale, long id) {
        log.info("Getting book change by id: {}", id);
        return bookRepository.findById(id)
                .map(book -> objectMapper.mapBookToBookChangeDto(locale, book));
    }

    @Override
    @Transactional
    public BookChangeFormValidationError saveBook(Locale locale, BookChangeDto bookDto) {
        log.info("Saving book: {}", bookDto);
        BookChangeFormValidationError errors = bookChangeValidator.validate(bookDto);
        checkBookIsbn(bookDto, errors);
        if (errors.isContainsErrors()) {
            return errors;
        }
        Optional<Book> bookOptional = Optional.of(bookDto)
                .map(bookChangeDto -> objectMapper.mapBookChangeDtoToBook(locale, bookChangeDto))
                .map(bookRepository::save);
        log.info("Book saved: {}", bookOptional);
        return errors;
    }

    private void checkBookIsbn(BookChangeDto bookDto, BookChangeFormValidationError errors) {
        if (bookDto.getId() == null) {
            if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
                errors.setIsbn(Validation.ISBN_EXISTS.getMessage());
            }
        } else {
            Optional<Book> book = bookRepository.findById(bookDto.getId());
            if (book.isPresent()
                    && !book.get().getIsbn().equals(bookDto.getIsbn())
                    && (bookRepository.existsByIsbn(bookDto.getIsbn()))) {
                errors.setIsbn(Validation.ISBN_EXISTS.getMessage());
            }
        }
    }

    @Override
    public Optional<BookDto> deleteBook(long id) {
        log.info("Deleting book by id: {}", id);
        Optional<Book> book = bookRepository.findById(id);
        List<Order> orders = orderRepository.findOrdersByBookId(id);
        if (book.isPresent() && orders.isEmpty()) {
            bookRepository.delete(book.get());
            return book.map(book1 -> objectMapper.mapBookToBookDto(book1, Locale.getDefault()));
        }
        return Optional.empty();
    }

    @Override
    public BookChangeFormValidationError updateBook(Locale locale, BookChangeDto bookDto) {
        log.info("Updating book: {}", bookDto);
        BookChangeFormValidationError errors = bookChangeValidator.validate(bookDto);
        checkBookIsbn(bookDto, errors);
        if (errors.isContainsErrors()) {
            return errors;
        }
        Optional<Book> bookOptional = Optional.of(bookDto)
                .map(bookChangeDto -> objectMapper.mapBookChangeDtoToBook(locale, bookChangeDto))
                .map(bookRepository::save);
        log.info("Book updated: {}", bookOptional);
        return errors;
    }

    @Override
    public Optional<BookChangeDto> getBookChangeByBookChangeDto(Locale locale, BookChangeDto bookDto) {
        log.info("Getting book change by book change dto: {}", bookDto);
        if (bookDto.getAuthors() == null || bookDto.getKeywords() == null) {
            return Optional.of(bookDto);
        }
        List<AuthorManagementDto> authors = bookDto.getAuthors().stream()
                .map(authorManagementDto -> {
                    Optional<Author> author = authorRepository.findById(authorManagementDto.getId());
                    if (author.isPresent()) {
                        return objectMapper.mapAuthorToAuthorChangeDto(author.get());
                    }
                    return authorManagementDto;
                })
                .toList();
        List<KeywordManagementDto> keywords = bookDto.getKeywords().stream()
                .map(keywordManagementDto -> {
                    Optional<Keyword> keyword = keywordRepository.findById(keywordManagementDto.getId());
                    if (keyword.isPresent()) {
                        return objectMapper.mapKeywordToKeywordChangeDto(keyword.get());
                    }
                    return keywordManagementDto;
                })
                .toList();
        bookDto.setAuthors(authors);
        bookDto.setKeywords(keywords);
        return Optional.of(bookDto);
    }

    @Override
    public Optional<BookDto> getBookById(long bookId, Locale locale, AuthorityUser authorityUser) {
        log.info("Getting book by bookId: {}", bookId);
        User user = userRepository.getByLogin(authorityUser.getLogin()).orElseThrow();
        Optional<Order> userOrder = orderRepository.findOrderByUserIdAndBookId(user.getId(), bookId);
        if (userOrder.isPresent()) {
            return Optional.empty();
        }
        return bookRepository.findById(bookId)
                .map(book1 -> objectMapper.mapBookToBookDto(book1, locale));
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
    public Page<BookDto> searchBooksExceptUserOrders(Pageable page, User user, String search, Locale locale) {
        log.info("Searching books except user orders: {} {} {}", page, user, search);
        User userWithOrders = userRepository.getByLogin(user.getLogin()).orElseThrow();
        if (search == null || search.isEmpty()) {
            if (page.getSort() == null || !page.getSort().isOrdered()) {
                Sort sort = Sort.by(Sort.Direction.ASC, "title");
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
            }
            Page<Book> booksExceptUserOrders = bookRepository.getBooksExceptUserOrders(page, userWithOrders);
            return objectMapper.mapBookPageToBookDtoPage(booksExceptUserOrders, locale);
        } else {
            Page<Book> bookPage = bookRepository.searchBooksExceptUserOrders(page, userWithOrders, search);
            return objectMapper.mapBookPageToBookDtoPage(bookPage, locale);
        }
    }

    @Override
    public Optional<Book> getBookByISBN(String isbn) {
        log.info("Getting book by ISBN: {}", isbn);
        return bookRepository.getBookByISBN(isbn);
    }

    @Override
    public Page<BookDto> searchBooks(Pageable page, Locale locale, String search) {
        log.info("Searching books: {} {}", page, search);
        Page<Book> bookPage;
        if (search == null || search.equals("")) {
            bookPage = bookRepository.findAll(page);
        } else {
            bookPage = bookRepository.searchBooks(page, search);
        }
        return objectMapper.mapBookPageToBookDtoPage(bookPage, locale);
    }

    @Override
    public boolean checkBookAvailability(long id) {
        log.info("Checking book availability: {}", id);
        Book book = bookRepository.findById(id).orElseThrow();
        return book.getCount() > 0;
    }
}

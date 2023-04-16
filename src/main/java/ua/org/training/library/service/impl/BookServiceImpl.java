package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.dto.BookDto;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.User;
import ua.org.training.library.repository.AuthorRepository;
import ua.org.training.library.repository.BookRepository;
import ua.org.training.library.repository.KeywordRepository;
import ua.org.training.library.repository.UserRepository;
import ua.org.training.library.service.BookService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.model.Keyword;

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

    @Override
    @Transactional
    public Book createModel(Book model) {
        log.info("Creating book: {}", model);
        List<Author> newAuthors = model.getAuthors().stream()
                .filter(author -> author.getId() == null)
                .toList();
        List<Keyword> newKeywords = model.getKeywords().stream()
                .filter(keyword -> keyword.getId() == null)
                .toList();
        if (!newAuthors.isEmpty()) {
            model.setAuthors(authorRepository.saveAll(model.getAuthors()));
        }
        if (!newKeywords.isEmpty()) {
            model.setKeywords(keywordRepository.saveAll(model.getKeywords()));
        }
        return bookRepository.save(model);
    }

    @Override
    @Transactional
    public void updateModel(Book model) {
        log.info("Updating book: {}", model);
        List<Author> newAuthors = model.getAuthors().stream()
                .filter(author -> author.getId() == null)
                .toList();
        List<Keyword> newKeywords = model.getKeywords().stream()
                .filter(keyword -> keyword.getId() == null)
                .toList();
        if (!newAuthors.isEmpty()) {
            model.setAuthors(authorRepository.saveAll(model.getAuthors()));
        }
        if (!newKeywords.isEmpty()) {
            model.setKeywords(keywordRepository.saveAll(model.getKeywords()));
        }
        bookRepository.save(model);
    }

    @Override
    @Transactional
    public void deleteModel(Book book) {
        log.info("Deleting book: {}", book);
        bookRepository.delete(book);
    }

    @Override
    public Optional<BookDto> getBookById(long id) {
        log.info("Getting book by id: {}", id);
        return bookRepository.findById(id)
                .map(objectMapper::mapBookToBookDto);
    }

    @Override
    @Transactional
    public void createModels(List<Book> models) {
        log.info("Creating books: {}", models);
        for (Book book : models) {
            List<Author> newAuthors = book.getAuthors().stream()
                    .filter(author -> author.getId() == null)
                    .toList();
            List<Keyword> newKeywords = book.getKeywords().stream()
                    .filter(keyword -> keyword.getId() == null)
                    .toList();
            if (!newAuthors.isEmpty()) {
                book.setAuthors(authorRepository.saveAll(book.getAuthors()));
            }
            if (!newKeywords.isEmpty()) {
                book.setKeywords(keywordRepository.saveAll(book.getKeywords()));
            }
        }
        bookRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void updateModels(List<Book> models) {
        log.info("Updating books: {}", models);
        for (Book book : models) {
            List<Author> newAuthors = book.getAuthors().stream()
                    .filter(author -> author.getId() == null)
                    .toList();
            List<Keyword> newKeywords = book.getKeywords().stream()
                    .filter(keyword -> keyword.getId() == null)
                    .toList();
            if (!newAuthors.isEmpty()) {
                book.setAuthors(authorRepository.saveAll(book.getAuthors()));
            }
            if (!newKeywords.isEmpty()) {
                book.setKeywords(keywordRepository.saveAll(book.getKeywords()));
            }
        }
        bookRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void deleteModels(List<Book> models) {
        log.info("Deleting books: {}", models);
        bookRepository.deleteAll(models);
    }

    @Override
    public List<Book> getAllModels() {
        log.info("Getting all books");
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getModelsByIds(List<Long> ids) {
        log.info("Getting books by ids: {}", ids);
        return bookRepository.findAllById(ids);
    }

    @Override
    public long countModels() {
        log.info("Counting books");
        return bookRepository.count();
    }

    @Override
    public void deleteAllModels() {
        log.info("Deleting all books");
        bookRepository.deleteAll();
    }

    @Override
    public boolean checkIfExists(Book model) {
        log.info("Checking if book exists: {}", model);
        return bookRepository.existsById(model.getId());
    }

    @Override
    public Page<Book> getModelsByPage(int pageNumber, int pageSize) {
        log.info("Getting books by page: {} {}", pageNumber, pageSize);
        return bookRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    @Transactional
    public void deleteModelById(Long id) {
        log.info("Deleting book by id: {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteModelsByIds(List<Long> ids) {
        log.info("Deleting books by ids: {}", ids);
        bookRepository.deleteAllById(ids);
    }

    @Override
    public List<Book> getAllModels(String sortField, String sortOrder) {
        log.info("Getting all books sorted by: {} {}", sortField, sortOrder);
        return bookRepository.findAll(Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()), sortField));
    }

    @Override
    public Page<Book> getModelsByPage(int pageNumber, int pageSize, Sort.Direction direction, String... sortField) {
        log.info("Getting books by page: {} {} {} {}", pageNumber, pageSize, direction, sortField);
        return bookRepository.findAll(PageRequest.of(pageNumber, pageSize, direction, sortField));
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

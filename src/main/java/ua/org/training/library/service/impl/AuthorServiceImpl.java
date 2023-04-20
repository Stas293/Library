package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.dto.AuthorDto;
import ua.org.training.library.dto.AuthorManagementDto;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.repository.AuthorRepository;
import ua.org.training.library.repository.BookRepository;
import ua.org.training.library.service.AuthorService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageImpl;

import java.util.List;
import java.util.Optional;

@Component
@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repository;
    private final ObjectMapper mapper;
    private final BookRepository bookRepository;

    @Override
    public List<AuthorManagementDto> getAllAuthors() {
        log.info("Getting all authors");
        List<Author> authors = repository.findAll();
        return authors.parallelStream()
                .map(mapper::mapAuthorToAuthorChangeDto)
                .toList();
    }

    @Override
    public List<AuthorManagementDto> searchAuthors(String search) {
        log.info("Searching authors by: {}", search);
        List<Author> authors = repository.findAllByNameContainingIgnoreCase(search);
        return authors.parallelStream()
                .map(mapper::mapAuthorToAuthorChangeDto)
                .toList();
    }

    @Override
    @Transactional
    public Optional<AuthorDto> createAuthor(AuthorManagementDto authorDto) {
        log.info("Creating author: {}", authorDto);
        Author author = mapper.mapAuthorChangeDtoToAuthor(authorDto);
        Author savedAuthor = repository.save(author);
        return Optional.of(mapper.mapAuthorToAuthorDto(savedAuthor));
    }

    @Override
    @Transactional
    public Optional<AuthorDto> deleteAuthor(long id) {
        log.info("Deleting author by id: {}", id);
        Optional<Author> author = repository.findById(id);
        if (author.isEmpty()) {
            return Optional.empty();
        }
        List<Book> books = bookRepository.findAllByAuthors(author.get());
        if (books.isEmpty()) {
            repository.deleteById(id);
            return author.map(mapper::mapAuthorToAuthorDto);
        }
        return Optional.empty();
    }

    @Override
    public Page<AuthorManagementDto> searchAuthors(Pageable pageable, String search) {
        log.info("Searching authors by: {}", search);
        Page<Author> authors;
        if (search.isEmpty()) {
            authors = repository.findAll(pageable);
        } else {
            authors = repository.searchAuthors(pageable, search);
        }
        return new PageImpl<>(authors.getContent().parallelStream()
                .map(mapper::mapAuthorToAuthorChangeDto)
                .toList(), pageable, authors.getTotalElements());
    }
}

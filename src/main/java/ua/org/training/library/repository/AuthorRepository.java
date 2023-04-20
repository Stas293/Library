package ua.org.training.library.repository;


import ua.org.training.library.model.Author;
import ua.org.training.library.repository.base.JRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.List;

public interface AuthorRepository extends JRepository<Author, Long> {
    Page<Author> searchAuthors(Pageable page, String search);

    List<Author> findAllByNameContainingIgnoreCase(String search);
}

package ua.org.training.library.service;


import ua.org.training.library.dto.AuthorDto;
import ua.org.training.library.dto.AuthorManagementDto;
import ua.org.training.library.form.AuthorSaveFormValidationError;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<AuthorManagementDto> getAllAuthors();

    List<AuthorManagementDto> searchAuthors(String search);

    AuthorSaveFormValidationError createAuthor(AuthorManagementDto authorDto);

    Optional<AuthorDto> deleteAuthor(long id);

    Page<AuthorManagementDto> searchAuthors(Pageable pageable, String search);

    Optional<AuthorManagementDto> getAuthor(long id);

    AuthorSaveFormValidationError updateAuthor(AuthorManagementDto authorDto);
}

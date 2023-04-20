package ua.org.training.library.service;


import ua.org.training.library.dto.KeywordDto;
import ua.org.training.library.dto.KeywordManagementDto;

import java.util.List;
import java.util.Optional;

public interface KeywordService {
    List<KeywordManagementDto> getKeywordsByQuery(String query);

    Optional<KeywordManagementDto> createKeyword(KeywordDto keyword);
}

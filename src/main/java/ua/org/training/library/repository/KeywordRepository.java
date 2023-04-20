package ua.org.training.library.repository;


import ua.org.training.library.model.Keyword;
import ua.org.training.library.repository.base.JRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JRepository<Keyword, Long> {
    List<Keyword> getKeywordsByQuery(String query);

    Optional<Keyword> findByData(String keyword);
}

package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.dto.KeywordDto;
import ua.org.training.library.dto.KeywordManagementDto;
import ua.org.training.library.model.Keyword;
import ua.org.training.library.repository.KeywordRepository;
import ua.org.training.library.service.KeywordService;
import ua.org.training.library.utility.mapper.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KeywordServiceImpl implements KeywordService {
    private final KeywordRepository repository;
    private final ObjectMapper mapper;

    @Override
    public List<KeywordManagementDto> getKeywordsByQuery(String query) {
        log.info("Getting keywords by query: {}", query);
        return repository.getKeywordsByQuery(query)
                .parallelStream()
                .map(mapper::mapKeywordToKeywordChangeDto)
                .toList();
    }

    @Override
    public Optional<KeywordManagementDto> createKeyword(KeywordDto keyword) {
        log.info("Creating keyword: {}", keyword);
        Optional<Keyword> keywordOptional = repository.findByData(keyword.getData());
        if (keywordOptional.isEmpty()) {
            Keyword newKeyword = mapper.mapKeywordDtoToKeyword(keyword);
            Keyword savedKeyword = repository.save(newKeyword);
            return Optional.of(mapper.mapKeywordToKeywordChangeDto(savedKeyword));
        }
        return Optional.empty();
    }
}

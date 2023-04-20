package ua.org.training.library.web.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Controller;
import ua.org.training.library.context.annotations.mapping.Get;
import ua.org.training.library.context.annotations.mapping.Post;
import ua.org.training.library.dto.KeywordDto;
import ua.org.training.library.dto.KeywordManagementDto;
import ua.org.training.library.service.KeywordService;
import ua.org.training.library.utility.mapper.JSONMapper;
import ua.org.training.library.utility.mapper.RequestParamsObjectMapper;

import java.util.List;
import java.util.Optional;

@Controller("/keywords")
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KeywordController {
    private final KeywordService keywordService;
    private final JSONMapper jsonMapper;
    private final RequestParamsObjectMapper requestParamsObjectMapper;

    @Get("/admin")
    @SneakyThrows
    public String getKeywordsByQuery(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get keywords by query");
        String query = request.getParameter("query");
        log.info("Query: {}", query);
        List<KeywordManagementDto> keywordManagementDtoList = keywordService.getKeywordsByQuery(query);
        jsonMapper.toJson(response.getWriter(), keywordManagementDtoList);
        return "";
    }

    @Post("/admin")
    @SneakyThrows
    public String addKeyword(HttpServletRequest request, HttpServletResponse response) {
        log.info("Add keyword");
        KeywordDto keyword = requestParamsObjectMapper.mapKeyword(request);
        log.info("Keyword: {}", keyword);
        Optional<KeywordManagementDto> keywordDto = keywordService.createKeyword(keyword);
        if (keywordDto.isEmpty()) {
            response.setStatus(400);
            return "";
        }
        jsonMapper.toJson(response.getWriter(), keywordDto);
        return "";
    }
}

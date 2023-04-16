package ua.org.training.library.dao;


import ua.org.training.library.model.Keyword;

import java.sql.Connection;
import java.util.List;

public interface KeywordDao extends GenericDao<Keyword> {
    List<Keyword> getKeywordsByBookId(Connection connection,
                                      Long id);

    void setKeywordsToBook(Connection connection,
                           Long bookId,
                           List<Keyword> keywords);
}

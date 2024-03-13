package ua.org.training.library.dao.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.dao.collectors.Collector;
import ua.org.training.library.enums.constants.AuthorQueries;
import ua.org.training.library.model.Author;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageImpl;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorDaoImplTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement statement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private AuthorQueries authorQueries;

    @Mock
    private Collector<Author> authorCollector;

    @InjectMocks
    private AuthorDaoImpl authorDao;

    @Test
    @SneakyThrows
    public void testGetAuthorsByBookId() {
        Long bookId = 123L;

        // Set up the mocks
        when(authorQueries.getAuthorsByBookId()).thenReturn("SELECT * FROM authors WHERE book_id = ?");
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(authorCollector.collectList(resultSet)).thenReturn(Arrays.asList(
                Author.builder().firstName("John").lastName("Doe").build(),
                Author.builder().firstName("Jane").lastName("Doe").build()
        ));

        // Call the method under test
        List<Author> authors = authorDao.getAuthorsByBookId(connection, bookId);

        // Verify that the DAO called the correct methods and returned the expected results
        verify(authorQueries).getAuthorsByBookId();
        verify(connection).prepareStatement("SELECT * FROM authors WHERE book_id = ?");
        verify(statement).setLong(1, bookId);
        verify(statement).executeQuery();
        verify(authorCollector).collectList(resultSet);
        assertEquals(2, authors.size());
        assertEquals("John", authors.get(0).getFirstName());
        assertEquals("Doe", authors.get(0).getLastName());
        assertEquals("Jane", authors.get(1).getFirstName());
        assertEquals("Doe", authors.get(1).getLastName());
    }

    @Test
    @SneakyThrows
    void searchAuthors() {
        String search = "test";
        List<Author> expectedAuthors = List.of(Author.builder().firstName(search).lastName(search).build());

        when(authorQueries.getFindAllByNameContainingIgnoreCase()).thenReturn("SELECT * FROM authors WHERE first_name ILIKE ? OR last_name ILIKE ? OR patronymic ILIKE ?");
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(authorCollector.collectList(resultSet)).thenReturn(expectedAuthors);

        List<Author> actualAuthors = authorDao.findAllByNameContainingIgnoreCase(connection, search);

        verify(connection).prepareStatement(authorQueries.getFindAllByNameContainingIgnoreCase());
        verify(statement).setString(1, "%" + search + "%");
        verify(statement).setString(2, "%" + search + "%");
        verify(statement).setString(3, "%" + search + "%");
        verify(statement).executeQuery();
        verify(authorCollector).collectList(resultSet);
        assertEquals(expectedAuthors, actualAuthors);
    }

    @Test
    @SneakyThrows
    void testSearchAuthors() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("firstName"));
        String search = "test";
        List<Author> expectedAuthors = List.of(Author.builder().firstName(search).lastName(search).build());
//queryBuilderImpl
//                            .select("*, count(*) OVER()")
//                            .from("authors")
//                            .where("first_name LIKE ?")
//                            .or("middle_name LIKE ?")
//                            .or("last_name LIKE ?")
//                            .limit("?")
//                            .offset("?")
//                            .build()
        String query = "SELECT *, count(*) OVER() FROM authors " +
                "WHERE first_name ILIKE ? OR middle_name LIKE ? OR last_name ILIKE ? " +
                "ORDER BY first_name ASC LIMIT ? OFFSET ?";
        when(authorQueries.getSearchAuthors(pageable.getSort())).thenReturn(query);
        when(connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(authorCollector.collectList(resultSet)).thenReturn(expectedAuthors);
        when(resultSet.getLong(5)).thenReturn(1L);

        Page<Author> actualAuthors = authorDao.searchAuthors(connection, pageable, search);

        verify(connection).prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        verify(statement).setString(1, "%" + search + "%");
        verify(statement).setString(2, "%" + search + "%");
        verify(statement).setString(3, "%" + search + "%");
        verify(statement).setInt(4, pageable.getPageSize());
        verify(statement).setLong(5, pageable.getOffset());
        verify(statement).executeQuery();
        verify(authorCollector).collectList(resultSet);
        assertEquals(expectedAuthors, actualAuthors.getContent());
        assertEquals(1, actualAuthors.getTotalElements());
    }

    @Test
    @SneakyThrows
    void findAllByNameContainingIgnoreCase() {
        String search = "test";
        List<Author> expectedAuthors = List.of(Author.builder().firstName(search).lastName(search).build());

        when(authorQueries.getFindAllByNameContainingIgnoreCase()).thenReturn("SELECT * FROM authors WHERE first_name ILIKE ? OR last_name ILIKE ? OR patronymic ILIKE ?");
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(authorCollector.collectList(resultSet)).thenReturn(expectedAuthors);

        List<Author> actualAuthors = authorDao.findAllByNameContainingIgnoreCase(connection, search);

        verify(connection).prepareStatement(authorQueries.getFindAllByNameContainingIgnoreCase());
        verify(statement).setString(1, "%" + search + "%");
        verify(statement).setString(2, "%" + search + "%");
        verify(statement).setString(3, "%" + search + "%");
        verify(statement).executeQuery();
        verify(authorCollector).collectList(resultSet);
        assertEquals(expectedAuthors, actualAuthors);
    }

    @Test
    @SneakyThrows
    void create() {
        Author author = Author.builder()
                .firstName("John")
                .middleName("Middle")
                .lastName("Doe")
                .build();
        when(authorQueries.getCreateAuthor()).thenReturn("INSERT INTO authors (first_name, middle_name, last_name) VALUES (?, ?, ?)");
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);
        when(statement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        Author actualAuthor = authorDao.create(connection, author);

        verify(connection).prepareStatement(authorQueries.getCreateAuthor(), Statement.RETURN_GENERATED_KEYS);
        verify(statement).setString(1, author.getFirstName());
        verify(statement).setString(2, author.getMiddleName());
        verify(statement).setString(3, author.getLastName());
        verify(statement).executeUpdate();
        verify(statement).getGeneratedKeys();
        verify(resultSet).next();
        verify(resultSet).getLong(1);
        assertEquals(1L, actualAuthor.getId());
        assertEquals(author.getFirstName(), actualAuthor.getFirstName());
        assertEquals(author.getMiddleName(), actualAuthor.getMiddleName());
        assertEquals(author.getLastName(), actualAuthor.getLastName());
    }

    @Test
    @SneakyThrows
    void testCreate() {
        List<Author> expectedAuthors = List.of(Author.builder().firstName("John").middleName("Middle").lastName("Doe").build());

        when(authorQueries.getCreateAuthor()).thenReturn("INSERT INTO authors (first_name, middle_name, last_name) VALUES (?, ?, ?)");
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        List<Author> actualAuthors = authorDao.create(connection, expectedAuthors);

        verify(connection).prepareStatement(authorQueries.getCreateAuthor(), Statement.RETURN_GENERATED_KEYS);
        verify(statement).setString(1, expectedAuthors.get(0).getFirstName());
        verify(statement).setString(2, expectedAuthors.get(0).getMiddleName());
        verify(statement).setString(3, expectedAuthors.get(0).getLastName());
        verify(statement).executeBatch();
        verify(statement).getGeneratedKeys();
        verify(resultSet).next();
        verify(resultSet).getLong(1);
        assertEquals(1L, actualAuthors.get(0).getId());
        assertEquals(expectedAuthors.get(0).getFirstName(), actualAuthors.get(0).getFirstName());
        assertEquals(expectedAuthors.get(0).getMiddleName(), actualAuthors.get(0).getMiddleName());
        assertEquals(expectedAuthors.get(0).getLastName(), actualAuthors.get(0).getLastName());
    }

    @Test
    @SneakyThrows
    void getById() {
        Long id = 1L;
        Author expectedAuthor = Author.builder().id(id).firstName("John").middleName("Middle").lastName("Doe").build();

        when(authorQueries.getAuthorById()).thenReturn("SELECT * FROM authors WHERE id = ?");
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(authorCollector.collect(resultSet)).thenReturn(expectedAuthor);

        Optional<Author> actualAuthor = authorDao.getById(connection, id);

        verify(connection).prepareStatement(authorQueries.getAuthorById());
        verify(statement).setLong(1, id);
        verify(statement).executeQuery();
        verify(resultSet).next();
        verify(authorCollector).collect(resultSet);
        assertEquals(expectedAuthor, actualAuthor.get());
    }

    @Test
    @SneakyThrows
    void getByIds() {
        List<Long> ids = List.of(1L, 2L, 3L);
        List<Author> expectedAuthors = List.of(Author.builder().id(1L).firstName("John").middleName("Middle").lastName("Doe").build(),
                Author.builder().id(2L).firstName("John").middleName("Middle").lastName("Doe").build(),
                Author.builder().id(3L).firstName("John").middleName("Middle").lastName("Doe").build());

        when(authorQueries.getAuthorsByIds(ids.size())).thenReturn("SELECT * FROM authors WHERE id = ANY (?)");
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(authorCollector.collectList(resultSet)).thenReturn(expectedAuthors);

        List<Author> actualAuthors = authorDao.getByIds(connection, ids);

        verify(connection).prepareStatement(authorQueries.getAuthorsByIds(ids.size()));
        verify(statement).setLong(1, ids.get(0));
        verify(statement).setLong(2, ids.get(1));
        verify(statement).setLong(3, ids.get(2));
        verify(statement).executeQuery();
        verify(authorCollector).collectList(resultSet);
        assertEquals(expectedAuthors, actualAuthors);
    }

    @Test
    @SneakyThrows
    void getAll() {
        List<Author> expectedAuthors = List.of(Author.builder().id(1L).firstName("John").middleName("Middle").lastName("Doe").build(),
                Author.builder().id(2L).firstName("John").middleName("Middle").lastName("Doe").build(),
                Author.builder().id(3L).firstName("John").middleName("Middle").lastName("Doe").build());
        when(authorQueries.getAllAuthors()).thenReturn("SELECT * FROM authors");
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(authorCollector.collectList(resultSet)).thenReturn(expectedAuthors);

        List<Author> actualAuthors = authorDao.getAll(connection);

        verify(connection).prepareStatement(authorQueries.getAllAuthors());
        verify(statement).executeQuery();
        verify(authorCollector).collectList(resultSet);
        assertEquals(expectedAuthors, actualAuthors);
    }

    @Test
    @SneakyThrows
    void testGetAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Author> expectedAuthors = List.of(Author.builder().id(1L).firstName("John").middleName("Middle").lastName("Doe").build(),
                Author.builder().id(2L).firstName("John").middleName("Middle").lastName("Doe").build(),
                Author.builder().id(3L).firstName("John").middleName("Middle").lastName("Doe").build());
        when(authorQueries.getAllAuthors(sort)).thenReturn("SELECT * FROM authors ORDER BY id ASC");
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(authorCollector.collectList(resultSet)).thenReturn(expectedAuthors);

        List<Author> actualAuthors = authorDao.getAll(connection, sort);

        verify(connection).prepareStatement(authorQueries.getAllAuthors(sort));
        verify(statement).executeQuery();
        verify(authorCollector).collectList(resultSet);
        assertEquals(expectedAuthors, actualAuthors);
    }

    @Test
    @SneakyThrows
    void getPage() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<Author> expectedPage = new PageImpl<>(
                List.of(
                        Author.builder()
                                .id(1L)
                                .firstName("John")
                                .middleName("Middle")
                                .lastName("Doe")
                                .build(),
                        Author.builder()
                                .id(2L)
                                .firstName("John")
                                .middleName("Middle")
                                .lastName("Doe")
                                .build()),
                pageable, 2);
        String selectAuthorQueries = "SELECT * FROM authors ORDER BY id ASC LIMIT ? OFFSET ?";
        when(authorQueries.getPageAuthors(pageable.getSort())).thenReturn(selectAuthorQueries);
        when(connection.prepareStatement(selectAuthorQueries, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(authorCollector.collectList(resultSet)).thenReturn(expectedPage.getContent());

        Page<Author> actualPage = authorDao.getPage(connection, pageable);

        verify(connection).prepareStatement(authorQueries.getPageAuthors(pageable.getSort()), ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        verify(statement).setInt(1, pageable.getPageSize());
        verify(statement).setLong(2, pageable.getOffset());
        verify(resultSet).getLong(5);
        verify(authorCollector).collectList(resultSet);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    @SneakyThrows
    void update() {
        Author entity = Author.builder().id(1L).firstName("John").middleName("Middle").lastName("Doe").build();
        when(authorQueries.getUpdateAuthor()).thenReturn("UPDATE authors SET first_name = ?, middle_name = ?, last_name = ? WHERE id = ?");
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        authorDao.update(connection, entity);

        verify(connection).prepareStatement(authorQueries.getUpdateAuthor());
        verify(statement).setString(1, entity.getFirstName());
        verify(statement).setString(2, entity.getMiddleName());
        verify(statement).setString(3, entity.getLastName());
        verify(statement).setLong(4, entity.getId());
        verify(statement).executeUpdate();
    }

    @Test
    @SneakyThrows
    void delete() {
        Long id = 1L;
        when(authorQueries.getDeleteAuthor()).thenReturn("DELETE FROM authors WHERE id = ?");
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        authorDao.delete(connection, id);

        verify(connection).prepareStatement(authorQueries.getDeleteAuthor());
        verify(statement).setLong(1, id);
        verify(statement).executeUpdate();
    }

    @Test
    @SneakyThrows
    void count() {
        when(authorQueries.getCountAuthors()).thenReturn("SELECT COUNT(*) FROM authors");
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(2L);

        long actualCount = authorDao.count(connection);

        verify(connection).prepareStatement(authorQueries.getCountAuthors());
        verify(resultSet).next();
        verify(resultSet).getLong(1);
        assertEquals(2L, actualCount);
    }

    @Test
    @SneakyThrows
    void deleteAll() {
        when(authorQueries.getDeleteAllAuthors()).thenReturn("TRUNCATE TABLE authors");
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        authorDao.deleteAll(connection);

        verify(connection).prepareStatement(authorQueries.getDeleteAllAuthors());
        verify(statement).executeUpdate();
    }

    @Test
    @SneakyThrows
    void testDeleteAll() {
        List<Long> longs = List.of(1L, 2L, 3L);
        when(authorQueries.getDeleteAuthorsByIds(longs.size())).thenReturn("DELETE FROM authors WHERE id IN (?, ?, ?)");
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        authorDao.deleteAll(connection, longs);

        verify(connection).prepareStatement(authorQueries.getDeleteAuthorsByIds(longs.size()));
        verify(statement).setLong(1, longs.get(0));
        verify(statement).setLong(2, longs.get(1));
        verify(statement).setLong(3, longs.get(2));
        verify(statement).executeUpdate();
    }

    @Test
    @SneakyThrows
    void testUpdate() {
        List<Author> entities = List.of(
                Author.builder().id(1L).firstName("John").middleName("Middle").lastName("Doe").build(),
                Author.builder().id(2L).firstName("John").middleName("Middle").lastName("Doe").build(),
                Author.builder().id(3L).firstName("John").middleName("Middle").lastName("Doe").build());
        when(authorQueries.getUpdateAuthor()).thenReturn("UPDATE authors SET first_name = ?, middle_name = ?, last_name = ? WHERE id = ?");
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        authorDao.update(connection, entities);

        verify(connection).prepareStatement(authorQueries.getUpdateAuthor());
        verify(statement, times(3)).setString(1, entities.get(0).getFirstName());
        verify(statement, times(3)).setString(2, entities.get(0).getMiddleName());
        verify(statement, times(3)).setString(3, entities.get(0).getLastName());
        verify(statement).setLong(4, entities.get(0).getId());
        verify(statement).setLong(4, entities.get(1).getId());
        verify(statement).setLong(4, entities.get(2).getId());
        verify(statement, times(3)).addBatch();
        verify(statement).executeBatch();
    }
}
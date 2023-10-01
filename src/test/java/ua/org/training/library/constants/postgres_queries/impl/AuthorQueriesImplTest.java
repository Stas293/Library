package ua.org.training.library.constants.postgres_queries.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.utility.page.impl.Sort;
import ua.org.training.library.utility.query.QueryBuilderImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorQueriesImplTest {
    @Mock
    private QueryBuilderImpl queryBuilder;

    @InjectMocks
    private AuthorQueriesImpl authorQueries;

    @Test
    void getAuthorsByBookId() {
        // given
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.select(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.from(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.join(anyString(), anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.where(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("SELECT * FROM authors a JOIN book_authors ab ON a.id = ab.author_id WHERE ab.book_id = ?");

        // when
        String result = authorQueries.getAuthorsByBookId();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).select("*");
        verify(queryBuilder).from("authors a");
        verify(queryBuilder).join("book_authors ab", "a.id = ab.author_id");
        verify(queryBuilder).where("ab.book_id = ?");
        verify(queryBuilder).build();
        assertEquals("SELECT * FROM authors a JOIN book_authors ab ON a.id = ab.author_id WHERE ab.book_id = ?", result);
    }

    @Test
    void getCreateAuthor() {
        // given
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.insertInto(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.columns(anyString(), anyString(), anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.values(anyString(), anyString(), anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("INSERT INTO authors (first_name, middle_name, last_name) VALUES (?, ?, ?)");

        // when
        String result = authorQueries.getCreateAuthor();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).insertInto("authors");
        verify(queryBuilder).columns("first_name", "middle_name", "last_name");
        verify(queryBuilder).values("?", "?", "?");
        verify(queryBuilder).build();
        assertEquals("INSERT INTO authors (first_name, middle_name, last_name) VALUES (?, ?, ?)", result);
    }

    @Test
    void getAuthorById() {
        // given
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.select(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.from(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.where(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("SELECT * FROM authors WHERE id = ?");

        // when
        String result = authorQueries.getAuthorById();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).select("*");
        verify(queryBuilder).from("authors");
        verify(queryBuilder).where("id = ?");
        verify(queryBuilder).build();
        assertEquals("SELECT * FROM authors WHERE id = ?", result);
    }

    @Test
    void getAllAuthors() {
        // given
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.select(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.from(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("SELECT * FROM authors");

        // when
        String result = authorQueries.getAllAuthors();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).select("*");
        verify(queryBuilder).from("authors");
        verify(queryBuilder).build();
        assertEquals("SELECT * FROM authors", result);
    }

    @Test
    void getPageAuthors() {
        // given
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.select(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.from(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.limit(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.offset(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("SELECT * FROM authors LIMIT ? OFFSET ?");

        // when
        String result = authorQueries.getPageAuthors(null);

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).select("*");
        verify(queryBuilder).from("authors");
        verify(queryBuilder).limit("?");
        verify(queryBuilder).offset("?");
        verify(queryBuilder).build();
        assertEquals("SELECT * FROM authors LIMIT ? OFFSET ?", result);
    }

    @Test
    void getUpdateAuthor() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.update(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.set(anyString(), anyString(), anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.where(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("UPDATE authors SET first_name = ?, middle_name = ?, last_name = ? WHERE id = ?");

        // when
        String result = authorQueries.getUpdateAuthor();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).update("authors");
        verify(queryBuilder).set("first_name", "middle_name", "last_name");
        verify(queryBuilder).where("id = ?");
        verify(queryBuilder).build();

        assertEquals("UPDATE authors SET first_name = ?, middle_name = ?, last_name = ? WHERE id = ?", result);
    }

    @Test
    void getDeleteAuthor() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.deleteFrom(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.where(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("DELETE FROM authors WHERE id = ?");

        // when
        String result = authorQueries.getDeleteAuthor();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).deleteFrom("authors");
        verify(queryBuilder).where("id = ?");
        verify(queryBuilder).build();
        assertEquals("DELETE FROM authors WHERE id = ?", result);
    }

    @Test
    void getAuthorsByIds() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.select(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.from(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.where(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.in(anyInt()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("SELECT * FROM authors WHERE id IN (?)");

        // when
        String result = authorQueries.getAuthorsByIds(1);

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).select("*");
        verify(queryBuilder).from("authors");
        verify(queryBuilder).where("id");
        verify(queryBuilder).in(1);
        verify(queryBuilder).build();
        assertEquals("SELECT * FROM authors WHERE id IN (?)", result);
    }

    @Test
    void getCountAuthors() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.select(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.from(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("SELECT count(*) FROM authors");

        // when
        String result = authorQueries.getCountAuthors();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).select("count(*)");
        verify(queryBuilder).from("authors");
        verify(queryBuilder).build();
        assertEquals("SELECT count(*) FROM authors", result);
    }

    @Test
    void getDeleteAllAuthors() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.truncate(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.cascade())
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("TRUNCATE authors CASCADE");

        // when
        String result = authorQueries.getDeleteAllAuthors();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).truncate("authors");
        verify(queryBuilder).cascade();
        verify(queryBuilder).build();
        assertEquals("TRUNCATE authors CASCADE", result);
    }

    @Test
    void testGetAllAuthors() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.select(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.from(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.orderBy(Mockito.any(String.class)))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("SELECT * FROM authors ORDER BY id ASC");

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        // when
        String result = authorQueries.getAllAuthors(sort);

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).select("*");
        verify(queryBuilder).from("authors");
        verify(queryBuilder).orderBy(Mockito.any(String.class));
        verify(queryBuilder).build();
        assertEquals("SELECT * FROM authors ORDER BY id ASC", result);
    }

    @Test
    void getDeleteAuthorsByIds() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.deleteFrom(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.where(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.in(anyInt()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("DELETE FROM authors WHERE id IN (?)");

        // when
        String result = authorQueries.getDeleteAuthorsByIds(1);

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).deleteFrom("authors");
        verify(queryBuilder).where("id");
        verify(queryBuilder).in(1);
        verify(queryBuilder).build();
        assertEquals("DELETE FROM authors WHERE id IN (?)", result);
    }

    @Test
    void getDeleteAuthorsByBookId() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.deleteFrom(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.where(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("DELETE FROM book_authors WHERE book_id = ?");

        // when
        String result = authorQueries.getDeleteAuthorsByBookId();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).deleteFrom("book_authors");
        verify(queryBuilder).where("book_id = ?");
        verify(queryBuilder).build();

        assertEquals("DELETE FROM book_authors WHERE book_id = ?", result);
    }

    @Test
    void getSaveAuthorsToBook() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.insertInto(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.columns(anyString(), anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.values(anyString(), anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)");

        // when
        String result = authorQueries.getSaveAuthorsToBook();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).insertInto("book_authors");
        verify(queryBuilder).columns("book_id", "author_id");
        verify(queryBuilder).values("?", "?");
        verify(queryBuilder).build();

        assertEquals("INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)", result);
    }

    @Test
    void getSearchAuthors() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.select(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.from(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.where(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.or(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.or(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.limit(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.offset(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("SELECT * FROM authors WHERE first_name LIKE ? OR middle_name LIKE ? OR last_name LIKE ? LIMIT ? OFFSET ?");

        // when
        String result = authorQueries.getSearchAuthors(null);

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).select("*");
        verify(queryBuilder).from("authors");
        verify(queryBuilder).where("first_name LIKE ?");
        verify(queryBuilder).or("middle_name LIKE ?");
        verify(queryBuilder).or("last_name LIKE ?");
        verify(queryBuilder).limit("?");
        verify(queryBuilder).offset("?");
        verify(queryBuilder).build();
assertEquals("SELECT * FROM authors WHERE first_name LIKE ? OR middle_name LIKE ? OR last_name LIKE ? LIMIT ? OFFSET ?", result);
    }

    @Test
    void getAuthorsCount() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.select(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.from(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.where(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.or(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.or(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("SELECT count(*) FROM authors WHERE first_name LIKE ? OR middle_name LIKE ? OR last_name LIKE ?");

        // when
        String result = authorQueries.getAuthorsCount();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).select("count(*)");
        verify(queryBuilder).from("authors");
        verify(queryBuilder).where("first_name LIKE ?");
        verify(queryBuilder).or("middle_name LIKE ?");
        verify(queryBuilder).or("last_name LIKE ?");
        verify(queryBuilder).build();
assertEquals("SELECT count(*) FROM authors WHERE first_name LIKE ? OR middle_name LIKE ? OR last_name LIKE ?", result);
    }

    @Test
    void getFindAllByNameContainingIgnoreCase() {
        when(queryBuilder.setUp())
                .thenReturn(queryBuilder);
        when(queryBuilder.select(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.from(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.where(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.or(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.or(anyString()))
                .thenReturn(queryBuilder);
        when(queryBuilder.build())
                .thenReturn("SELECT * FROM authors WHERE first_name LIKE ? OR middle_name LIKE ? OR last_name LIKE ?");

        // when
        String result = authorQueries.getFindAllByNameContainingIgnoreCase();

        // then
        verify(queryBuilder).setUp();
        verify(queryBuilder).select("*");
        verify(queryBuilder).from("authors");
        verify(queryBuilder).where("first_name LIKE ?");
        verify(queryBuilder).or("middle_name LIKE ?");
        verify(queryBuilder).or("last_name LIKE ?");
        verify(queryBuilder).build();
        assertEquals("SELECT * FROM authors WHERE first_name LIKE ? OR middle_name LIKE ? OR last_name LIKE ?", result);
    }
}
INSERT INTO authors (first_name, last_name, middle_name)
VALUES ('John', 'Doe', 'Paul'),
       ('Jane', 'Doe', NULL),
       ('Alice', 'Smith', 'Marie');

INSERT INTO books (title, description, isbn, date_publication, fine, count, language, location)
VALUES ('The Great Gatsby', 'A novel by F. Scott Fitzgerald', '978-3-16-148410-0', '1925-04-10', 2.99, 10, 'English',
        'New York'),
       ('To Kill a Mockingbird', 'A novel by Harper Lee', '978-3-16-148410-1', '1960-07-11', 3.99, 20, 'English',
        'Alabama'),
       ('Pride and Prejudice', 'A novel by Jane Austen', '978-3-16-148410-2', '1813-01-28', 1.99, 5, 'English',
        'London');


INSERT INTO book_authors (author_id, book_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (3, 3);


INSERT INTO keywords (keyword)
VALUES ('Romance'),
       ('Mystery'),
       ('Thriller');


INSERT INTO book_keywords (book_id, keyword_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 1);

\c library

INSERT INTO roles (code, name)
VALUES ('USER', 'User'),
       ('ADMIN', 'Admin'),
       ('LIBRARIAN', 'Librarian');

INSERT INTO statuses (code, closed)
VALUES ('REGISTER', FALSE),
       ('ACCEPT', FALSE),
       ('REJECT', TRUE),
       ('CLOSE', TRUE);

INSERT INTO next_statuses (next_status, status)
VALUES (1, 2),
       (1, 3),
       (2, 4);

INSERT INTO status_name (name, lang, status_id)
VALUES ('Registered', 'en', 1),
       ('Accepted', 'en', 2),
       ('Rejected', 'en', 3),
       ('Closed', 'en', 4);

INSERT INTO places (code, default_days, choosable)
VALUES ('AT_LIBRARY', 1, FALSE),
       ('AT_HOME', 14, TRUE);

INSERT INTO place_names (lang, name, place_id)
VALUES ('en', 'At library', 1),
       ('en', 'At home', 2);

INSERT INTO authors (first_name, last_name, middle_name)
VALUES ('John', 'Doe', 'Paul'),
       ('Jane', 'Doe', NULL),
       ('Alice', 'Smith', 'Marie');

INSERT INTO books (title, description, isbn, date_publication, fine, count, language, location)
VALUES ('The Great Gatsby', 'A novel by F. Scott Fitzgerald', '9783161484100', '1925-04-10', 2.99, 10, 'English',
        'New York'),
       ('To Kill a Mockingbird', 'A novel by Harper Lee', '9783161484101', '1960-07-11', 3.99, 20, 'English',
        'Alabama'),
       ('Pride and Prejudice', 'A novel by Jane Austen', '9783161484102', '1813-01-28', 1.99, 5, 'English',
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

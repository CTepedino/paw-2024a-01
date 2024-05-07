INSERT INTO users (email, password) VALUES ('repeatedEmail@error.com', 'password');
INSERT INTO roles (user_id, role) VALUES (1, 'READER');

INSERT INTO users (email, password) VALUES ('anotherMail@mail.com', '12345678');
INSERT INTO roles (user_id, role) VALUES (2, 'WRITER');

INSERT INTO books (title, description, genre, page_count, price, suggested_age, published_date, writer_id)
VALUES ('my book', 'a book that I wrote', 'FICTION', 456, 1575.23, 10, now(), 2), ('my book 2', 'another book that I wrote', 'FICTION', 557, 1990.15, 10, now(), 2);

INSERT INTO cover_images (id, file) VALUES (1,'');
INSERT INTO book_previews (id, file) VALUES (1,'');
INSERT INTO book_files (id, file) VALUES (1,'');

INSERT INTO orders (buyer_id, book_id, status) VALUES (1, 1, 'WAITING_CONTACT');

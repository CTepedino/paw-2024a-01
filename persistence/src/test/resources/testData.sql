INSERT INTO users (email, password) VALUES ('repeatedEmail@error.com', 'password');
INSERT INTO roles (user_id, role) VALUES (1, 'READER');

INSERT INTO users (email, password) VALUES ('anotherMail@mail.com', '12345678');
INSERT INTO roles (user_id, role) VALUES (2, 'WRITER');

INSERT INTO books (title, description, genre, page_count, price, suggested_age, published_date, writer_id, is_paused)
VALUES ('my book', 'a book that I wrote', 'FICTION', 456, 1575.23, 10, now(), 2, FALSE), ('my book 2', 'another book that I wrote', 'FICTION', 557, 1990.15, 10, now(), 2, FALSE);

INSERT INTO cover_images (id, file) VALUES (1,'');
INSERT INTO book_previews (id, file) VALUES (1,'');
INSERT INTO book_files (id, file) VALUES (1,'');

INSERT INTO orders (order_id, buyer_id, book_id, status) VALUES (1, 1, 2, 'WAITING_CONTACT');
INSERT INTO orders (order_id, buyer_id, book_id, status) VALUES (2, 1, 2, 'WAITING_CONTACT');

INSERT INTO users (email, password, cbu) VALUES ('booksPaused@mail.com', '12345678', '123');
INSERT INTO roles (user_id, role) VALUES (3, 'WRITER');

INSERT INTO books (title, description, genre, page_count, price, suggested_age, writer_id, is_paused) VALUES ('','','FICTION', 1,1.02,1,3,TRUE);

INSERT INTO email_validations (id, code, expiration) VALUES (1, '12345', NOW());

INSERT INTO orders (buyer_id, book_id, status) VALUES (3, 1, 'COMPLETED');

INSERT INTO reviews (reviewer_id, book_id, rating, review) VALUES (3, 1, 6, 'its a book');
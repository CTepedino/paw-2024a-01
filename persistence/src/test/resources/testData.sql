INSERT INTO users (user_id, email, password, is_enabled) VALUES (101, 'repeatedEmail@error.com', 'password', true);
INSERT INTO roles (user_id, role) VALUES (101, 'READER');

INSERT INTO users (user_id, email, password, is_enabled) VALUES (102, 'anotherMail@mail.com', '12345678', true);
INSERT INTO roles (user_id, role) VALUES (102, 'WRITER');

INSERT INTO books (book_id, title, description, genre, page_count, price, suggested_age, published_date, writer_id, is_paused, sales_category)
VALUES (101, 'my book', 'a book that I wrote', 'FICTION', 456, 1575.23, 10, now(), 102, FALSE, 'DEFAULT'), (102, 'my book 2', 'another book that I wrote', 'FICTION', 557, 1990.15, 10, now(), 102, FALSE, 'DEFAULT');

INSERT INTO cover_images (id, file) VALUES (101,'');
INSERT INTO book_previews (id, file) VALUES (101,'');
INSERT INTO book_files (id, file) VALUES (101,'');

INSERT INTO users (user_id, email, password, cbu, is_enabled) VALUES (103, 'booksPaused@mail.com', '12345678', '123', true);
INSERT INTO roles (user_id, role) VALUES (103, 'WRITER');
INSERT INTO email_validations (id, code, expiration) VALUES (101, '12345', NOW());
INSERT INTO reset_codes (id, code, expiration) VALUES (101, '12345', NOW());

INSERT INTO books (book_id, title, description, genre, page_count, price, suggested_age, writer_id, is_paused, sales_category) VALUES (103, '','','FICTION', 1,1.02,1,103,TRUE, 'DEFAULT');


INSERT INTO orders (order_id, buyer_id, book_id, status, price, is_public) VALUES (101, 102, 103, 'WAITING_APPROVAL', 10, false);
INSERT INTO orders (order_id, buyer_id, book_id, status, price, is_public) VALUES (102, 103, 102, 'COMPLETED', 10, true);

INSERT INTO reviews (review_id, reviewer_id, book_id, rating, review) VALUES (101, 103, 102, 6, 'its a book');

INSERT INTO questions (question_id, date, question, book_id, questioner_id) VALUES (101, NOW(), 'is this a question?', 101, 101);

INSERT INTO deals (id, end_date, price, start_date) VALUES (101, NOW(), 5, NOW())
/* Sprint 2 modifications:

DROP TABLE users; (era la tabla que se creó en el ejemplo de la clase teórica, no tenía datos)
ALTER TABLE IF EXISTS writers RENAME TO users;

ALTER TABLE users RENAME COLUMN writer_id TO user_id;
ALTER TABLE users ADD COLUMN password VARCHAR(255);
ALTER TABLE users ALTER COLUMN first_name DROP NOT NULL;
ALTER TABLE users ALTER COLUMN last_name DROP NOT NULL;
ALTER TABLE books DROP COLUMN writer_name;
ALTER TABLE books DROP COLUMN writer_last_name;
ALTER TABLE books DROP COLUMN writer_email; (no se pierden datos, la misma información ya se podía acceder mediante el writer_id en la tabla users)
*/

/* Sprint 3 modifications:
ALTER TABLE pdfs RENAME COLUMN pdf_id TO id;
ALTER TABLE pdfs RENAME COLUMN pdf TO file;
ALTER TABLE pdfs RENAME TO book_previews;

ALTER TABLE images RENAME COLUMN image_id TO id;
ALTER TABLE images RENAME COLUMN image TO file;
ALTER TABLE images RENAME TO cover_images;

ALTER TABLE books RENAME COLUMN pdf_id TO preview_id;
ALTER TABLE books RENAME COLUMN image_id TO cover_id;

ALTER TABLE orders DROP CONSTRAINT orders_pkey;
ALTER TABLE orders DROP COLUMN writer_id;
ALTER TABLE orders ADD COLUMN date TIMESTAMP default now();
UPDATE orders SET date = now() WHERE date IS NULL;
ALTER TABLE orders ADD COLUMN order_id SERIAL PRIMARY KEY;
UPDATE orders SET order_id = DEFAULT;


ALTER TABLE book_previews ADD COLUMN book_id INT;
UPDATE book_previews AS bp SET book_id = b.book_id FROM books AS b WHERE bp.id = b.preview_id;
ALTER TABLE books DROP preview_id;
ALTER TABLE book_previews DROP CONSTRAINT pdfs_pkey;
ALTER TABLE book_previews DROP COLUMN id;
ALTER TABLE book_previews RENAME COLUMN book_id TO id;
ALTER TABLE book_previews ADD PRIMARY KEY (id);
ALTER TABLE book_previews ADD CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES books (book_id) ON DELETE CASCADE;

ALTER TABLE cover_images ADD COLUMN book_id INT;
UPDATE cover_images AS ci SET book_id = b.book_id FROM books AS b WHERE ci.id = b.cover_id;
ALTER TABLE books DROP cover_id;
ALTER TABLE cover_images DROP CONSTRAINT images_pkey;
ALTER TABLE cover_images DROP COLUMN id;
ALTER TABLE cover_images RENAME COLUMN book_id TO id;
ALTER TABLE cover_images ADD PRIMARY KEY (id);
ALTER TABLE cover_images ADD CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES books (book_id) ON DELETE CASCADE;

ALTER TABLE users ADD COLUMN cbu VARCHAR(22);
ALTER TABLE users ADD COLUMN is_enabled BOOLEAN DEFAULT TRUE;
UPDATE users SET is_enabled = TRUE WHERE is_enabled IS NULL;
ALTER TABLE users ADD COLUMN locale VARCHAR(10) DEFAULT 'en';
UPDATE users SET locale = 'en' WHERE locale is NULL;

CREATE TABLE IF NOT EXISTS book_files(
     id INT PRIMARY KEY REFERENCES books (book_id) ON DELETE CASCADE,
     file BYTEA NOT NULL
);

ALTER TABLE books ADD COLUMN is_paused BOOLEAN DEFAULT FALSE;
UPDATE books b SET is_paused = CASE
       WHEN NOT EXISTS(
           SELECT 1
           FROM book_files bf
           WHERE bf.id = b.book_id
       ) OR EXISTS(
           SELECT 1
           FROM users AS u
           WHERE u.user_id = b.writer_id AND u.cbu IS NULL
       ) THEN TRUE
       ELSE FALSE
END;
*/

/* Sprint 4 modifications:
ALTER TABLE orders ADD COLUMN is_public BOOLEAN DEFAULT FALSE;
ALTER TABLE payment_receipts ADD COLUMN type VARCHAR(20) NOT NULL DEFAULT 'application/pdf';
ALTER TABLE users ADD COLUMN description TEXT;
*/

/* Sprint 5 modifications:
ALTER TABLE books ALTER COLUMN title TYPE VARCHAR(50);
ALTER TABLE books ALTER COLUMN genre TYPE VARCHAR(40);

ALTER TABLE reviews DROP CONSTRAINT reviews_pkey;
ALTER TABLE reviews ADD COLUMN review_id SERIAL PRIMARY KEY;
UPDATE reviews SET review_id = DEFAULT;
ALTER TABLE reviews ADD CONSTRAINT reviews_unique UNIQUE (reviewer_id, book_id);

ALTER TABLE orders ADD CONSTRAINT orders_unique UNIQUE (book_id, buyer_id);

ALTER TABLE reviews DROP CONSTRAINT reviews_rating_check;
ALTER TABLE reviews ADD CONSTRAINT reviews_rating_check CHECK (rating BETWEEN 0 AND 10);

ALTER TABLE orders ADD COLUMN rejected_reason VARCHAR(255);
*/

/* Sprint 6 modifications:
ALTER TABLE books ADD COLUMN sales_category VARCHAR(40) NOT NULL DEFAULT 'DEFAULT';

CREATE OR REPLACE VIEW book_order_totals AS
SELECT b.book_id, COUNT(o.order_id) AS total_orders
FROM books b
LEFT JOIN orders o ON b.book_id = o.book_id
GROUP BY b.book_id;

UPDATE books b
SET sales_category =
    CASE
        WHEN w.total_orders >= 3 THEN 'BEST_SELLER'
        WHEN w.total_orders >= 1 THEN 'POPULAR'
        ELSE 'DEFAULT'
    END
FROM book_order_totals w
WHERE b.book_id = w.book_id;

ALTER TABLE orders ADD COLUMN price DECIMAL(10, 2);
UPDATE orders o
SET price = (
    SELECT b.price
    FROM books b
    WHERE b.book_id = o.book_id
);

ALTER TABLE users ADD COLUMN writer_category VARCHAR(40) DEFAULT 'DEFAULT';

CREATE OR REPLACE VIEW writer_order_totals AS
SELECT u.user_id, COUNT(o.order_id) AS total_orders
FROM users u
LEFT JOIN books b ON u.user_id = b.writer_id
LEFT JOIN orders o ON b.book_id = o.book_id
GROUP BY u.user_id;

UPDATE users u
SET writer_category =
    CASE
        WHEN w.total_orders >= 20 THEN 'GOLD'
        WHEN w.total_orders >= 10 THEN 'SILVER'
        WHEN w.total_orders >= 5 THEN 'BRONZE'
        ELSE 'DEFAULT'
    END
FROM writer_order_totals w
WHERE u.user_id = w.user_id;

DROP VIEW writer_order_totals;
DROP VIEW book_order_totals;

 */

CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    cbu VARCHAR(22),
    is_enabled BOOLEAN,
    locale VARCHAR(10) DEFAULT 'en',
    description TEXT,
    writer_category VARCHAR(40) DEFAULT 'DEFAULT'
);

CREATE TABLE IF NOT EXISTS books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    genre VARCHAR(40) NOT NULL,
    page_count INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    suggested_age INT NOT NULL,
    published_date DATE DEFAULT now(),
    writer_id INT NOT NULL,
    is_paused BOOLEAN DEFAULT FALSE,
    sales_category VARCHAR(40) NOT NULL DEFAULT 'DEFAULT',

    FOREIGN KEY (writer_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS profile_pictures(
    id INT PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
    file BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS book_previews(
    id INT PRIMARY KEY REFERENCES books (book_id) ON DELETE CASCADE,
    file BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS cover_images(
    id INT PRIMARY KEY REFERENCES books (book_id) ON DELETE CASCADE,
    file BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS book_files(
    id INT PRIMARY KEY REFERENCES books (book_id) ON DELETE CASCADE,
    file BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS roles(
    user_id INT NOT NULL,
    role VARCHAR(20) NOT NULL,

    PRIMARY KEY (user_id, role),

    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders(
    order_id SERIAL PRIMARY KEY,
    buyer_id INT NOT NULL,
    book_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    date TIMESTAMP default now(),
    is_public BOOLEAN DEFAULT FALSE,
    rejected_reason VARCHAR(255),
    price DECIMAL(10, 2),

    FOREIGN KEY (buyer_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (book_id) ON DELETE CASCADE,

    UNIQUE(buyer_id, book_id)
);

CREATE TABLE IF NOT EXISTS payment_receipts(
    id INT PRIMARY KEY REFERENCES orders (order_id) ON DELETE CASCADE,
    file BYTEA NOT NULL,
    type VARCHAR(20) NOT NULL DEFAULT 'application/pdf'
);

CREATE TABLE IF NOT EXISTS reviews(
    review_id SERIAL PRIMARY KEY,
    reviewer_id INT NOT NULL,
    book_id INT NOT NULL,
    rating INT NOT NULL,
    review TEXT,
    date TIMESTAMP default now(),

    CHECK (rating BETWEEN 0 AND 10),

    FOREIGN KEY (reviewer_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (book_id) ON DELETE CASCADE,

    UNIQUE(book_id, reviewer_id)
);

CREATE TABLE IF NOT EXISTS email_validations(
    id INT PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
    code VARCHAR(5) NOT NULL,
    expiration TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS reset_codes(
    id INT PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
    code VARCHAR(5) NOT NULL,
    expiration TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS wishlist(
    id SERIAL PRIMARY KEY,
    user_id INT,
    book_id INT,

    UNIQUE (book_id, user_id),

    FOREIGN KEY (book_id) REFERENCES books (book_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS questions(
    question_id SERIAL PRIMARY KEY,
    book_id INT NOT NULL,
    questioner_id INT NOT NULL,
    question TEXT NOT NULL,
    answer TEXT,
    question_date TIMESTAMP default now(),
    answer_date TIMESTAMP,

    FOREIGN KEY (questioner_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (book_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS deals(
    id INT PRIMARY KEY,
    price DECIMAL(10, 2) NOT NULL,
    start_date TIMESTAMP default now(),
    end_date TIMESTAMP,

    FOREIGN KEY (id) REFERENCES books (book_id) ON DELETE CASCADE
);
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

/* Spring 3 modifications:
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
ALTER TABLE book_previews DROP CONSTRAINT book_previews_pkey;
ALTER TABLE book_previews DROP COLUMN id;
ALTER TABLE book_previews RENAME COLUMN book_id TO id;
ALTER TABLE book_previews ADD PRIMARY KEY (id);
ALTER TABLE book_previews ADD CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES books (book_id) ON DELETE CASCADE;

ALTER TABLE cover_images ADD COLUMN book_id INT;
UPDATE cover_images AS ci SET book_id = b.book_id FROM books AS b WHERE ci.id = b.cover_id;
ALTER TABLE books DROP cover_id;
ALTER TABLE cover_images DROP CONSTRAINT cover_images_pkey;
ALTER TABLE cover_images DROP COLUMN id;
ALTER TABLE cover_images RENAME COLUMN book_id TO id;
ALTER TABLE cover_images ADD PRIMARY KEY (id);
ALTER TABLE cover_images ADD CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES books (book_id) ON DELETE CASCADE;

ALTER TABLE users ADD COLUMN cbu VARCHAR(22);
*/



CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    cbu VARCHAR(22)
);

CREATE TABLE IF NOT EXISTS books (
    book_id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    genre TEXT NOT NULL,
    page_count INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    suggested_age INT NOT NULL,
    published_date DATE DEFAULT now(),
    writer_id INT NOT NULL,

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

    FOREIGN KEY (buyer_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (book_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS payment_receipts(
    id INT PRIMARY KEY REFERENCES orders (order_id) ON DELETE CASCADE,
    file BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews(
    reviewer_id INT NOT NULL,
    book_id INT NOT NULL,
    rating INT NOT NULL,
    review TEXT,
    date TIMESTAMP default now(),

    CHECK (rating BETWEEN 1 AND 10),

    PRIMARY KEY (book_id, reviewer_id),

    FOREIGN KEY (reviewer_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (book_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS email_validations(
    id INT PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
    code VARCHAR(5) NOT NULL,
    expiration TIMESTAMP NOT NULL
);
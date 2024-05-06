/* Sprint 2 modifications:

DROP TABLE users; (era la tabla que se creó en el ejemplo de la clase teorica, no tenía datos)
ALTER TABLE IF EXISTS writers RENAME TO users;

ALTER TABLE users RENAME COLUMN writer_id TO user_id;
ALTER TABLE users ADD COLUMN password VARCHAR(255);
ALTER TABLE users ALTER COLUMN first_name DROP NOT NULL;
ALTER TABLE users ALTER COLUMN last_name DROP NOT NULL;
ALTER TABLE books DROP COLUMN writer_name;
ALTER TABLE books DROP COLUMN writer_last_name;
ALTER TABLE books DROP COLUMN writer_email; (no se pierden datos, la misma información ya se podia acceder mediante el writer_id en la tabla users)
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
ALTER TABLE orders ADD PRIMARY KEY (buyer_id, book_id);
ALTER TABLE orders ADD COLUMN date TIMESTAMP default now();
UPDATE orders SET date = now() WHERE date IS NULL;
*/

CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS book_previews(
    id SERIAL PRIMARY KEY,
    file BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS cover_images(
    id SERIAL PRIMARY KEY,
    file BYTEA NOT NULL
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
    preview_id INT NOT NULL,
    cover_id INT NOT NULL,
    writer_id INT NOT NULL,

    FOREIGN KEY (writer_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (cover_id) REFERENCES cover_images (id) ON DELETE CASCADE,
    FOREIGN KEY (preview_id) REFERENCES book_previews (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roles(
    user_id INT NOT NULL,
    role VARCHAR(20) NOT NULL,

    PRIMARY KEY (user_id, role),

    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders(
    buyer_id INT NOT NULL,
    book_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    date DATE default now(),

    PRIMARY KEY (book_id, buyer_id),

    FOREIGN KEY (buyer_id) REFERENCES  users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (book_id) ON DELETE CASCADE
);

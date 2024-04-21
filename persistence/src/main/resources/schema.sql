/* Sprint 2 modifications:

DROP TABLE users;
ALTER TABLE IF EXISTS writers RENAME TO users;
ALTER TABLE users RENAME COLUMN writer_id TO user_id;
*/

CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS pdfs(
    pdf_id SERIAL PRIMARY KEY,
    pdf BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS images(
    image_id SERIAL PRIMARY KEY,
    image BYTEA NOT NULL
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
    pdf_id INT NOT NULL,
    image_id INT NOT NULL,
    writer_id INT NOT NULL,

    writer_name TEXT,
    writer_last_name TEXT,
    writer_email TEXT,

    FOREIGN KEY (writer_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES images (image_id) ON DELETE CASCADE,
    FOREIGN KEY (pdf_id) REFERENCES pdfs (pdf_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roles(
    user_id INT NOT NULL,
    role VARCHAR(20) NOT NULL,

    PRIMARY KEY (user_id, role),

    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS writers(
    writer_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS pdfs(
    pdf_id SERIAL PRIMARY KEY,
    pdf BYTEA
);

CREATE TABLE IF NOT EXISTS images(
    image_id SERIAL PRIMARY KEY,
    image BYTEA
);

CREATE TABLE IF NOT EXISTS books (
    book_id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    genre TEXT,
    page_count INT,
    price DECIMAL(10, 2),
    suggested_age INT,
    published_date DATE,
    pdf_preview_id INT,
    image_id INT,
    writer_id INT,
    FOREIGN KEY (writer_id) REFERENCES writers (writer_id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES images (image_id) ON DELETE CASCADE,
    FOREIGN KEY (pdf_preview_id) REFERENCES pdfs (pdf_id) ON DELETE CASCADE
);




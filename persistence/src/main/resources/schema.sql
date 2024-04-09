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
    FOREIGN KEY (writer_id) REFERENCES writers (writer_id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES images (image_id) ON DELETE CASCADE,
    FOREIGN KEY (pdf_id) REFERENCES pdfs (pdf_id) ON DELETE CASCADE
);




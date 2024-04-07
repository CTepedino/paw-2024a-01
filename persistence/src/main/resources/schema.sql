CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS images (
    image_id SERIAL PRIMARY KEY,
    photoblob BYTEA
);

CREATE TABLE IF NOT EXISTS writers (
    writer_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS books (
    book_id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    genre TEXT,
    preview TEXT,
    page_numbers INT,
    price INT,
    image_id INT,
    suggested_age INT,
    published_date DATE,
    writer_id INT,
    FOREIGN KEY (writer_id) REFERENCES writers (writer_id)
    );




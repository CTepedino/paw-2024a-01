CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS Book (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    genra VARCHAR(100),
    prev VARCHAR(100),
    page_numbers INT,
    price DECIMAL(10,2),
    image VARCHAR(255),
    suggested_age INT,
    published_date DATE,
    writer_email VARCHAR(255)
);

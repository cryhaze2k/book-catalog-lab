DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE books (
                       id SERIAL PRIMARY KEY,  -- <-- ЗМІНЕНО: IDENTITY -> SERIAL
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       publication_year INT NOT NULL,
                       description VARCHAR(1000)
);
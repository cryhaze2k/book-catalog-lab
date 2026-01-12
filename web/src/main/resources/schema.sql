DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE books (
                       id IDENTITY PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       publication_year INT NOT NULL,  -- Renamed to be safe
                       description VARCHAR(1000)
);
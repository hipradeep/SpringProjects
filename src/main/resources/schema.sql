-- Use DROP TABLE to ensure relationship is set up correctly during development
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS products;
-- Use CREATE TABLE IF NOT EXISTS to preserve data across restarts
-- Ensure the table was dropped manually once to apply the SERIAL fix
-- CREATE TABLE IF NOT EXISTS products (
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    qty INT,
    price DOUBLE PRECISION
);

CREATE TABLE tags (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    product_id INT REFERENCES products(id) ON DELETE CASCADE
);

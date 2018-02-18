DROP TABLE IF EXISTS users;
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY auto_increment,
    username VARCHAR(128) UNIQUE,
    password VARCHAR(256),
    enabled BOOL,
    roles VARCHAR(200)
);

DROP TABLE IF EXISTS customers;
CREATE TABLE customers (
    customer_id BIGINT PRIMARY KEY auto_increment,
    first_name VARCHAR(32) NOT NULL,
    last_name VARCHAR(128) NOT NULL,
    ndi VARCHAR(12) NOT NULL
);

DROP TABLE IF EXISTS images;
CREATE TABLE images (
    image_id BIGINT PRIMARY KEY auto_increment,
    customer_id BIGINT REFERENCES customers (customer_id),
    file_name VARCHAR(200) NOT NULL,
    file_data CLOB NOT NULL
);

DROP TABLE IF EXISTS products;
CREATE TABLE products (
    product_id BIGINT PRIMARY KEY auto_increment,
    ref VARCHAR(20) NOT NULL,
    name VARCHAR(200) NOT NULL,
    common_price DOUBLE NOT NULL
);

DROP TABLE IF EXISTS purchases;
CREATE TABLE purchases (
    purchase_id BIGINT PRIMARY KEY auto_increment,
    customer_id BIGINT REFERENCES customers (customer_id),
    product_id BIGINT REFERENCES products (product_id),
    purchase_date TIMESTAMP NOT NULL,
    num_items INTEGER NOT NULL,
    item_price DOUBLE NOT NULL
);

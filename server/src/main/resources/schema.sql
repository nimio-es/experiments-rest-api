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

DROP TABLE IF EXISTS skills;
CREATE TABLE purchases (
    purchase_id BIGINT PRIMARY KEY auto_increment,
    customer_id BIGINT REFERENCES customers (customer_id),
    purchase_date TIMESTAMP,
    amount BIGINT
);

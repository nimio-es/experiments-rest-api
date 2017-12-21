DROP TABLE IF EXISTS users;
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY auto_increment,
    username VARCHAR(128) UNIQUE,
    password VARCHAR(256),
    enabled BOOL,
);

DROP TABLE IF EXISTS customers;
CREATE TABLE customers (
    customer_id BIGINT PRIMARY KEY auto_increment,
    first_name VARCHAR(32),
    last_name VARCHAR(128),
    ndi VARCHAR(12),
);

DROP TABLE IF EXISTS skills;
CREATE TABLE purchases (
    purchase_id BIGINT PRIMARY KEY auto_increment,
    customer_id BIGINT REFERENCES customers (customer_id),
    purchase_date TIMESTAMP,
    amount BIGINT
);

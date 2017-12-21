INSERT INTO users (user_id, username, password, enabled) VALUES 
	(1, 'saulo.alvarado', '$2a$10$D4OLKI6yy68crm.3imC9X.P2xqKHs5TloWUcr6z5XdOqnTrAK84ri', true),
	(2, 'noadmin', '$2a$10$D4OLKI6yy68crm.3imC9X.P2xqKHs5TloWUcr6z5XdOqnTrAK84ri', true);

INSERT INTO customers (customer_id, first_name, last_name, ndi) VALUES
	(1, 'Saulo', 'Alvarado Mateos', '000000000X'),
	(2, 'Nieves', 'Sánchez Marrero', '111111111Y');

INSERT INTO purchases (purchase_id, customer_id, purchase_date, amount) VALUES
    (1, 1, '2017-12-25', 200),
    (2, 1, '2017-12-26', 300),
    (3, 1, '2017-12-27', 58),
    (4, 2, '2017-12-13', 125)
INSERT INTO users (username,password,enabled) VALUES ('admin','admin', TRUE );
INSERT INTO authorities VALUES ('admin','admin');

INSERT INTO users (username,password,enabled) VALUES ('manoli','manoli', TRUE );
INSERT INTO authorities VALUES ('manoli','client');
INSERT INTO users (username,password,enabled) VALUES ('david','david', TRUE );
INSERT INTO authorities VALUES ('david','client');

INSERT INTO users (username,password,enabled) VALUES ('paco','paco', TRUE );
INSERT INTO authorities VALUES ('paco','usuario');
INSERT INTO users (username,password,enabled) VALUES ('lolo','lolo', TRUE );
INSERT INTO authorities VALUES ('lolo','usuario');
INSERT INTO users (username,password,enabled) VALUES ('pepe','pepe', TRUE );
INSERT INTO authorities VALUES ('pepe','usuario');

INSERT INTO administrators (id, username) VALUES (1, 'admin');

INSERT INTO usuarios (id, nombre, apellidos, dni, direccion, telefono, email, username) VALUES (2, 'Paco', 'Naranjo', '21154416G', 'C/Esperanza', '666973647', 'Paco@gmail.com','paco');
INSERT INTO usuarios (id, nombre, apellidos, dni, direccion, telefono, email, username) VALUES (3, 'Lolo', 'Lopez', '25486596L', 'C/Macarena', '690670547' ,'Lolo@gmail.com','lolo');
INSERT INTO usuarios (id, nombre, apellidos, dni, direccion, telefono, email, username) VALUES (4, 'Pepe', 'Lopez', '12456776V', 'C/Macarena', '690670547', 'Pepe@gmail.com','pepe');

INSERT INTO clients (id, name, email, address, init, finish, telephone, description, code, food, username) VALUES (1,'bar manoli','manoli@gmail.com','C/Betis','10:00','22:00','608726190', 'description 1', 'code1', 'ESPAÑOLA','manoli');
INSERT INTO clients (id, name, email, address, init, finish, telephone, description, code, food, username)  VALUES (2,'bar david','david@gmail.com','C/Sevilla','09:30','22:00','608726190', 'description 2', 'code2', 'americana','david');

INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-14 12:00:00', '2021-08-15 12:00:00', 'FO-1', 'inactive', 1, 'macarrones', 15);
INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'FO-2', 'active', 1, 'macarrones con tomate', 10);
INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-16 12:00:00', '2021-08-17 12:00:00', null, 'hidden', 1, 'macarrones con queso', 5);

INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-14 12:00:00', '2021-08-15 12:00:00', 'T-1', 'inactive', 1, '12:00:00', '13:00:00', 5);
INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'T-2', 'active', 1, '12:00:00', '13:00:00', 10);
INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-16 12:00:00', '2021-08-17 12:00:00', null, 'hidden', 1, '12:00:00', '13:00:00', 15);

INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-14 12:00:00', '2021-08-15 12:00:00', 'SP-1', 'inactive',1,5,25,10,15,15,10);
INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'SP-2', 'active',1,5,25,10,15,15,10);
INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-16 12:00:00', '2021-08-17 12:00:00', null, 'hidden',1,5,25,10,15,15,10);

INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-14 12:00:00', '2021-08-15 12:00:00', 'NU-1', 'inactive',1,15,25,10,15,5,10);
INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'NU-2', 'active',1,15,25,10,15,5,10);
INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-16 12:00:00', '2021-08-17 12:00:00', null, 'hidden',1,15,25,10,15,5,10);

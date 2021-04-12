INSERT INTO users (username,password,enabled) VALUES ('admin','admin', TRUE );
INSERT INTO authorities (username,authority) VALUES ('admin','admin');

INSERT INTO users (username,password,enabled) VALUES ('manoli','manoli', TRUE );
INSERT INTO authorities (username,authority) VALUES ('manoli','client');
INSERT INTO users (username,password,enabled) VALUES ('david','david', TRUE );
INSERT INTO authorities (username,authority) VALUES ('david','client');

INSERT INTO users (username,password,enabled) VALUES ('paco','paco', TRUE );
INSERT INTO authorities (username,authority) VALUES ('paco','usuario');
INSERT INTO users (username,password,enabled) VALUES ('lolo','lolo', TRUE );
INSERT INTO authorities (username,authority) VALUES ('lolo','usuario');
INSERT INTO users (username,password,enabled) VALUES ('pepe','pepe', TRUE );
INSERT INTO authorities (username,authority) VALUES ('pepe','usuario');


INSERT INTO usuarios (id, nombre, apellidos, direccion, municipio, email, username) VALUES (1, 'admin', 'admin',  'C/admin', 'carmona', 'admin@gmail.com','admin');
INSERT INTO usuarios (id, nombre, apellidos, direccion, municipio, email, username) VALUES (2, 'Paco', 'Naranjo', 'C/Esperanza', 'sevilla', 'Paco@gmail.com','paco');
INSERT INTO usuarios (id, nombre, apellidos, direccion, municipio, email, username) VALUES (3, 'Lolo', 'Lopez',  'C/Macarena', 'dos_hermanas', 'Lolo@gmail.com','lolo');
INSERT INTO usuarios (id, nombre, apellidos, direccion, municipio, email, username) VALUES (4, 'Pepe', 'Lopez', 'C/Macarena', 'carmona', 'Pepe@gmail.com','pepe');

INSERT INTO codes (id,code,activo) VALUES (1,'code1',FALSE);
INSERT INTO codes (id,code,activo) VALUES (2,'code2',FALSE);
INSERT INTO codes (id,code,activo) VALUES (3,'code3',TRUE);
INSERT INTO codes (id,code,activo) VALUES (4,'code4',TRUE);

INSERT INTO clients (id, name, email, address, municipio, init, finish, telephone, description, food, username, code) VALUES (1,'bar manoli','manoli@gmail.com','C/Betis', 'sevilla','10:00','22:00','608726190', 'description 1', 'ESPAÑOLA','manoli', 'code1');
INSERT INTO clients (id, name, email, address, municipio, init, finish, telephone, description, food, username, code)  VALUES (2,'bar david','david@gmail.com','C/Sevilla', 'dosHermanas','09:30','22:00','608726190', 'description 2', 'americana','david', 'code2');


INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-14 12:00:00', '2021-08-15 12:00:00', 'FO-1', 'inactive', 1, 'macarrones', 15);
INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'FO-2', 'active', 1, 'macarrones con tomate', 10);
INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'FO-3', 'active', 1, 'Estofado', 10);
INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'FO-4', 'active', 1, 'Puchero', 10);
INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'FO-5', 'active', 2, 'Tumbalobos', 10);
INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'FO-6', 'active', 2, 'Tortilla', 10);
INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'FO-7', 'active', 2, 'Arroz con leche', 10);
INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-08-16 12:00:00', '2021-08-17 12:00:00', null, 'hidden', 1, 'macarrones con queso', 5);

INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-14 12:00:00', '2021-08-15 12:00:00', 'T-1', 'inactive', 1, '12:00:00', '13:00:00', 5);
INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'T-2', 'active', 1, '12:00:00', '13:00:00', 10);
INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'T-3', 'active', 1, '12:30:00', '14:30:00', 10);
INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'T-4', 'active', 1, '12:00:00', '13:00:00', 5);
INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'T-5', 'active', 2, '13:00:00', '16:00:00', 15);
INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'T-6', 'active', 2, '14:00:00', '17:00:00', 15);
INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'T-7', 'active', 2, '11:00:00', '20:00:00', 20);
INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-08-16 12:00:00', '2021-08-17 12:00:00', null, 'hidden', 1, '12:00:00', '13:00:00', 15);

INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-14 12:00:00', '2021-08-15 12:00:00', 'SP-1', 'inactive',1,5,25,10,15,15,10);
INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'SP-2', 'active',1,35,25,40,15,55,10);
INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'SP-3', 'active',1,25,25,30,15,35,10);
INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'SP-4', 'active',1,15,25,20,15,35,10);
INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'SP-5', 'active',2,15,30,20,15,50,5);
INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'SP-6', 'active',2,15,30,21,15,50,5);
INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'SP-7', 'active',2,15,30,22,15,50,5);
INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-16 12:00:00', '2021-08-17 12:00:00', null, 'hidden',1,5,25,10,15,15,10);

INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-14 12:00:00', '2021-08-15 12:00:00', 'NU-1', 'inactive',1,15,25,10,15,5,10);
INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'NU-2', 'active',1,15,25,10,15,5,10);
INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'NU-3', 'active',1,15,25,12,15,3,5);
INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'NU-4', 'active',1,15,25,13,15,2,5);
INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'NU-5', 'active',2,20,35,15,15,5,5);
INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'NU-6', 'active',2,20,30,15,10,10,5);
INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-15 12:00:00', '2021-08-16 12:00:00', 'NU-7', 'active',2,20,35,15,15,10,5);
INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-08-16 12:00:00', '2021-08-17 12:00:00', null, 'hidden',1,15,25,10,15,5,10);

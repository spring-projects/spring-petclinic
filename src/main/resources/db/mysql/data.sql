INSERT INTO owners VALUES (1, 'Javi', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT INTO users (dtype,username,password,enabled) VALUES ('User','admin','admin', TRUE );
INSERT INTO authorities VALUES ('admin','admin');
INSERT INTO users (dtype,username,password,enabled) VALUES ('User','manoli','manoli', TRUE );
INSERT INTO authorities VALUES ('manoli','cliente');
INSERT INTO users (dtype,username,password,enabled) VALUES ('User','david','david', TRUE );
INSERT INTO authorities VALUES ('david','cliente');
INSERT INTO users (dtype,username,password,enabled) VALUES ('User','paco','paco', TRUE );
INSERT INTO authorities VALUES ('paco','usuario');
INSERT INTO users (dtype,username,password,enabled) VALUES ('User','lolo','lolo', TRUE );
INSERT INTO authorities VALUES ('lolo','usuario');
INSERT INTO users (dtype,username,password,enabled) VALUES ('User','pepe','pepe', TRUE );
INSERT INTO authorities VALUES ('pepe','usuario');

INSERT INTO usuarios VALUES (1, 'admin', 'admin', 'admin', 'C/admin', '000000000', 'admin@gmail.com','admin');
INSERT INTO usuarios VALUES (2, 'Paco', 'Naranjo', '21154416G', 'C/Esperanza', '666973647', 'Paco@gmail.com','paco');
INSERT INTO usuarios VALUES (3, 'Lolo', 'Lopez', '25486596L', 'C/Macarena', '690670547' ,'Lolo@gmail.com','lolo');
INSERT INTO usuarios VALUES (4, 'Pepe', 'Lopez', '12456776V', 'C/Macarena', '690670547', 'Pepe@gmail.com','pepe');

INSERT INTO clients (id, name, email, address, init, finish, telephone, description, code, food, username) VALUES (1,'bar manoli','manoli@gmail.com','C/Betis','10:00','22:00','608726190', 'description 1', 'code1', 'ESPAÑOLA','manoli');
INSERT INTO clients (id, name, email, address, init, finish, telephone, description, code, food, username)  VALUES (2,'bar david','david@gmail.com','C/Sevilla','09:30','22:00','608726190', 'description 2', 'code2', 'americana','david');

INSERT INTO food_offers(start, end, code, status, client_id, food, discount) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'FO-1', 'active', 1, 'macarrones', 15);
INSERT INTO time_offers(start, end, code, status, client_id, init, finish, discount) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'T-1', 'active', 1, '12:00:00', '13:00:00', 10);
INSERT INTO speed_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'SP-1', 'active',1,5,25,10,15,15,10);
INSERT INTO nu_offers(start, end, code, status, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'NU-1', 'active',1,15,25,10,15,5,10);


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


INSERT INTO food_offers(start, end, code, type, client_id, food, discount, units) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'jkhlljk', 'active', null, 'macarrones', '15%', 10);

INSERT INTO time_offers(start, end, code, type, client_id, init, finish, discount) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'jkhlljk', 'active', null, '12:00:00', '13:00:00', '10%');
INSERT INTO speed_offers(start, end, code, type, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'jkhlljk', 'active', null,5,'25%',10,'15%',15,'10%' );
INSERT INTO nu_offers(start, end, code, type, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'jkhlljk', 'active', null,15,'25%',10,'15%',5,'10%' );

--insert into usuarios(username, password, enabled) values ('admin3', 'admin', true);
--insert into authorities(id ,usuario, authority) values (42,'admin3', 'admin');

--INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
--INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');

INSERT INTO users (dtype,username,password,enabled) VALUES ('user','admin','admin', TRUE );
INSERT INTO authorities VALUES ('admin','admin');
INSERT INTO users (dtype,username,password,enabled) VALUES ('user','manoli','manoli', TRUE );
INSERT INTO authorities VALUES ('manoli','cliente');
INSERT INTO users (dtype,username,password,enabled) VALUES ('user','david','david', TRUE );
INSERT INTO authorities VALUES ('david','cliente');
INSERT INTO users (dtype,username,password,enabled) VALUES ('user','paco','paco', TRUE );
INSERT INTO authorities VALUES ('paco','usuario');
INSERT INTO users (dtype,username,password,enabled) VALUES ('user','lolo','lolo', TRUE );
INSERT INTO authorities VALUES ('lolo','usuario');
INSERT INTO users (dtype,username,password,enabled) VALUES ('user','pepe','pepe', TRUE );
INSERT INTO authorities VALUES ('pepe','usuario');

INSERT INTO usuarios VALUES (1, 'admin', 'admin', 'admin', 'C/admin', '000000000', 'admin@gmail.com','admin');

INSERT INTO clients  VALUES (1,'manoli@gmail.com','C/Betis','10:00','22:00','608726190', 'description 1', 'code1', 'ESPAÑOLA','manoli');
INSERT INTO clients  VALUES (2,'david@gmail.com','C/Sevilla','09:30','22:00','608726190', 'description 2', 'code2', 'americana','david');

INSERT INTO usuarios VALUES (2, 'Paco', 'Naranjo', '21154416G', 'C/Esperanza', '666973647', 'Paco@gmail.com','paco');
INSERT INTO usuarios VALUES (3, 'Lolo', 'Lopez', '25486596L', 'C/Macarena', '690670547' ,'Lolo@gmail.com','lolo');
INSERT INTO usuarios VALUES (4, 'Pepe', 'Lopez', '12456776V', 'C/Macarena', '690670547', 'Pepe@gmail.com','pepe');

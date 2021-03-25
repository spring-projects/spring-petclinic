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



--insert into usuarios(username, password, enabled) values ('admin3', 'admin', true);
--insert into authorities(id ,usuario, authority) values (42,'admin3', 'admin');

INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');

INSERT INTO clients(username,password,enabled, email, address, timetable,telephone,description,code,food) VALUES ('cliente','cliente',TRUE,'cliente@hotmail.com','Calle Tahona nº5','12:00-23:00','954876351','Descripcion','codigo','variado');

INSERT INTO users(username,password,enabled) VALUES ('cliente','cliente',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'cliente','client');

INSERT INTO time_offers(start, end, code, type, client_id, init, finish, discount) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'jkhlljk', 'active', 'cliente', '12:00:00', '13:00:00', '10%');

INSERT INTO clients(username,password,enabled, email, address, timetable,telephone,description,code,food) VALUES ('cliente2','cliente2',TRUE,'cliente@hotmail.com','Calle Tahona nº5','12:00-23:00','954876351','Descripcion','codigo','variado');

INSERT INTO users(username,password,enabled) VALUES ('cliente2','cliente2',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'cliente2','client');
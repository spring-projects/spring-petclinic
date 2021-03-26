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


INSERT INTO food_offers(start, end, code, type, client_id, food, discount, units) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'F-001', 'active', null, 'macarrones', '15%', 10);
INSERT INTO time_offers(start, end, code, type, client_id, init, finish, discount) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'T-001', 'active', null, '12:00:00', '13:00:00', '10%');
INSERT INTO speed_offers(start, end, code, type, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'SP-001', 'active', null,5,'25%',10,'15%',15,'10%' );
INSERT INTO nu_offers(start, end, code, type, client_id, gold, discount_gold, silver, discount_silver, bronze, discount_bronze) VALUES ('2021-06-15 12:00:00', '2021-06-16 12:00:00', 'NU-001', 'active', null,15,'25%',10,'15%',5,'10%' );

INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');


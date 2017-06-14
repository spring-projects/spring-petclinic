INSERT INTO doctors VALUES (1, 'James', 'Carter');
INSERT INTO doctors VALUES (2, 'Helen', 'Leary');
INSERT INTO doctors VALUES (3, 'Linda', 'Douglas');
INSERT INTO doctors VALUES (4, 'Rafael', 'Ortega');
INSERT INTO doctors VALUES (5, 'Henry', 'Stevens');
INSERT INTO doctors VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO doctor_specialties VALUES (2, 1);
INSERT INTO doctor_specialties VALUES (3, 2);
INSERT INTO doctor_specialties VALUES (3, 3);
INSERT INTO doctor_specialties VALUES (4, 2);
INSERT INTO doctor_specialties VALUES (5, 1);

INSERT INTO gender VALUES (1, 'male');
INSERT INTO gender VALUES (2, 'female');


INSERT INTO parents VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT INTO parents VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT INTO parents VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT INTO parents VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT INTO parents VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT INTO parents VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT INTO parents VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT INTO parents VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT INTO parents VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT INTO parents VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT INTO kids VALUES (1, 'Alyssa', '2000-09-07', 2, 1);
INSERT INTO kids VALUES (2, 'Joe', '2002-08-06', 1, 2);
INSERT INTO kids VALUES (3, 'Lauren', '2001-04-17', 2, 3);
INSERT INTO kids VALUES (4, 'Nicole', '2000-03-07', 2, 3);
INSERT INTO kids VALUES (5, 'Thomas', '2000-11-30', 1, 4);
INSERT INTO kids VALUES (6, 'Samantha', '2000-01-20', 2, 5);
INSERT INTO kids VALUES (7, 'George', '1995-09-04', 1, 6);
INSERT INTO kids VALUES (8, 'Max', '1995-09-04', 1, 6);
INSERT INTO kids VALUES (9, 'Brendan', '1999-08-06', 1, 7);
INSERT INTO kids VALUES (10, 'Elizabeth', '1997-02-24', 2, 8);
INSERT INTO kids VALUES (11, 'Lucy', '2000-03-09', 2, 9);
INSERT INTO kids VALUES (12, 'Sunny', '2000-06-24', 2, 10);
INSERT INTO kids VALUES (13, 'Conner', '2002-06-08', 1, 10);

INSERT INTO visits VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits VALUES (3, 8, '2013-01-03', 'cold');
INSERT INTO visits VALUES (4, 7, '2013-01-04', 'flu');

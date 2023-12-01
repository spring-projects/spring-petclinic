INSERT INTO vets VALUES (default, 'James', 'Carter');
INSERT INTO vets VALUES (default, 'Helen', 'Leary');
INSERT INTO vets VALUES (default, 'Linda', 'Douglas');
INSERT INTO vets VALUES (default, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (default, 'Henry', 'Stevens');
INSERT INTO vets VALUES (default, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (default, 'radiology');
INSERT INTO specialties VALUES (default, 'surgery');
INSERT INTO specialties VALUES (default, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (default, 'cat');
INSERT INTO types VALUES (default, 'dog');
INSERT INTO types VALUES (default, 'lizard');
INSERT INTO types VALUES (default, 'snake');
INSERT INTO types VALUES (default, 'bird');
INSERT INTO types VALUES (default, 'hamster');

INSERT INTO owners VALUES (default, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT INTO owners VALUES (default, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT INTO owners VALUES (default, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT INTO owners VALUES (default, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT INTO owners VALUES (default, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT INTO owners VALUES (default, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT INTO owners VALUES (default, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT INTO owners VALUES (default, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT INTO owners VALUES (default, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT INTO owners VALUES (default, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT INTO pets VALUES (default, 'Leo', TO_DATE('2010-09-07', 'YYYY-MM-DD'), 1, 1);
INSERT INTO pets VALUES (default, 'Basil', TO_DATE('2012-08-06', 'YYYY-MM-DD'), 6, 2);
INSERT INTO pets VALUES (default, 'Rosy', TO_DATE('2011-04-17', 'YYYY-MM-DD'), 2, 3);
INSERT INTO pets VALUES (default, 'Jewel', TO_DATE('2010-03-07', 'YYYY-MM-DD'), 2, 3);
INSERT INTO pets VALUES (default, 'Iggy', TO_DATE('2010-11-30', 'YYYY-MM-DD'), 3, 4);
INSERT INTO pets VALUES (default, 'George', TO_DATE('2010-01-20', 'YYYY-MM-DD'), 4, 5);
INSERT INTO pets VALUES (default, 'Samantha', TO_DATE('2012-09-04', 'YYYY-MM-DD'), 1, 6);
INSERT INTO pets VALUES (default, 'Max', TO_DATE('2012-09-04', 'YYYY-MM-DD'), 1, 6);
INSERT INTO pets VALUES (default, 'Lucky', TO_DATE('2011-08-06', 'YYYY-MM-DD'), 5, 7);
INSERT INTO pets VALUES (default, 'Mulligan', TO_DATE('2007-02-24', 'YYYY-MM-DD'), 2, 8);
INSERT INTO pets VALUES (default, 'Freddy', TO_DATE('2010-03-09', 'YYYY-MM-DD'), 5, 9);
INSERT INTO pets VALUES (default, 'Lucky', TO_DATE('2010-06-24', 'YYYY-MM-DD'), 2, 10);
INSERT INTO pets VALUES (default, 'Sly', TO_DATE('2012-06-08', 'YYYY-MM-DD'), 1, 10);

INSERT INTO visits VALUES (default, 7, TO_DATE('2013-01-01', 'YYYY-MM-DD'), 'rabies shot');
INSERT INTO visits VALUES (default, 8, TO_DATE('2013-01-02', 'YYYY-MM-DD'), 'rabies shot');
INSERT INTO visits VALUES (default, 8, TO_DATE('2013-01-03', 'YYYY-MM-DD'), 'neutered');
INSERT INTO visits VALUES (default, 7, TO_DATE('2013-01-04', 'YYYY-MM-DD'), 'spayed');

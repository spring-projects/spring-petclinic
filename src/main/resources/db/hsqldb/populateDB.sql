INSERT INTO vets VALUES (1, 'James', 'Carter', 'Graduated from Oxford University in 2000 having "gone the long way around" picking up degrees in Agriculture & Applied Zoology and also in Animal Production along the way. He came down to work in Winchester in 2004 and set up PetClinic in 2005.');
INSERT INTO vets VALUES (2, 'Helen', 'Leary', 'MA VetMB MRCVS A Cambridge graduate, Helen joined our expanding team of vets at the end of 2005. Helen has a cat called Checkers and a dog called Scoot.');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas', 'MA VetMB MRCVS Another Cambridge graduate, Linda joined our team in 2004. Linda has a hamster called Ash and a dog called Spotty.');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega','MA VetMB MRCVS Rafael graduated from Cambridge in 2003 and joined PetClinit in October 2004. He has a particular interest in horseback archery and has competed in many international competitions.');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens', 'MA VetMB MRCVS Henry graduated from Oxford in 2008 and joined us in 2010. He is also a competitive chess player and has participated in many international competitions.');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins','MA VetMB MRCVS Sharon graduated from Oxford in 2006 and joined us in 2007. She joins international salsa competitions and consider herself an accomplished dancer.');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', NULL);
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', NULL);
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', NULL);
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', NULL);
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', NULL);
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', NULL);
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', NULL);
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', NULL);
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', NULL);
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', NULL);

INSERT INTO pets VALUES (1, 'Leo', '2010-09-07', 1, 1, NULL);
INSERT INTO pets VALUES (2, 'Basil', '2012-08-06', 6, 2, NULL);
INSERT INTO pets VALUES (3, 'Rosy', '2011-04-17', 2, 3, NULL);
INSERT INTO pets VALUES (4, 'Jewel', '2010-03-07', 2, 3, NULL);
INSERT INTO pets VALUES (5, 'Iggy', '2010-11-30', 3, 4, NULL);
INSERT INTO pets VALUES (6, 'George', '2010-01-20', 4, 5, NULL);
INSERT INTO pets VALUES (7, 'Samantha', '2012-09-04', 1, 6, NULL);
INSERT INTO pets VALUES (8, 'Max', '2012-09-04', 1, 6, NULL);
INSERT INTO pets VALUES (9, 'Lucky', '2011-08-06', 5, 7, NULL);
INSERT INTO pets VALUES (10, 'Mulligan', '2007-02-24', 2, 8, NULL);
INSERT INTO pets VALUES (11, 'Freddy', '2010-03-09', 5, 9, NULL);
INSERT INTO pets VALUES (12, 'Lucky', '2010-06-24', 2, 10, NULL);
INSERT INTO pets VALUES (13, 'Sly', '2012-06-08', 1, 10, NULL);

INSERT INTO visits VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits VALUES (4, 7, '2013-01-04', 'spayed');

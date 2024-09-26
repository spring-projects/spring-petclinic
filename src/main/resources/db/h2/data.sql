INSERT INTO vets (id, first_name, last_name) VALUES (default, 'James', 'Carter');
INSERT INTO vets (id, first_name, last_name) VALUES (default, 'Helen', 'Leary');
INSERT INTO vets (id, first_name, last_name) VALUES (default, 'Linda', 'Douglas');
INSERT INTO vets (id, first_name, last_name) VALUES (default, 'Rafael', 'Ortega');
INSERT INTO vets (id, first_name, last_name) VALUES (default, 'Henry', 'Stevens');
INSERT INTO vets (id, first_name, last_name) VALUES (default, 'Sharon', 'Jenkins');

INSERT INTO specialties (id, name) VALUES (default, 'radiology');
INSERT INTO specialties (id, name) VALUES (default, 'surgery');
INSERT INTO specialties (id, name) VALUES (default, 'dentistry');

INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (2, 1);
INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (3, 2);
INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (3, 3);
INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (4, 2);
INSERT INTO vet_specialties (vet_id, specialty_id) VALUES (5, 1);

INSERT INTO types (id, name) VALUES (default, 'cat');
INSERT INTO types (id, name) VALUES (default, 'dog');
INSERT INTO types (id, name) VALUES (default, 'lizard');
INSERT INTO types (id, name) VALUES (default, 'snake');
INSERT INTO types (id, name) VALUES (default, 'bird');
INSERT INTO types (id, name) VALUES (default, 'hamster');

INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (default, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (default, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (default, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (default, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (default, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (default, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (default, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (default, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (default, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT INTO owners (id, first_name, last_name, address, city, telephone) VALUES (default, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'George', '2010-01-20', 4, 5);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets (id, name, birth_date, type_id, owner_id) VALUES (default, 'Sly', '2012-06-08', 1, 10);

INSERT INTO visits (id, pet_id, visit_date, description) VALUES (default, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits (id, pet_id, visit_date, description) VALUES (default, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits (id, pet_id, visit_date, description) VALUES (default, 8, '2013-01-03', 'neutered');
INSERT INTO visits (id, pet_id, visit_date, description) VALUES (default, 7, '2013-01-04', 'spayed');

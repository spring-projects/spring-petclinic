INSERT INTO vets (first_name, last_name, uuid)
SELECT 'James', 'Carter', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM vets WHERE id=1);

INSERT INTO vets (first_name, last_name, uuid)
SELECT 'Helen', 'Leary', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM vets WHERE id=2);

INSERT INTO vets (first_name, last_name, uuid)
SELECT 'Linda', 'Douglas', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM vets WHERE id=3);

INSERT INTO vets (first_name, last_name, uuid)
SELECT 'Rafael', 'Ortega', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM vets WHERE id=4);

INSERT INTO vets (first_name, last_name, uuid)
SELECT 'Henry', 'Stevens', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM vets WHERE id=5);

INSERT INTO vets (first_name, last_name, uuid)
SELECT 'Sharon', 'Jenkins', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM vets WHERE id=6);

INSERT INTO specialties (name, uuid)
SELECT 'radiology', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM specialties WHERE name='radiology');

INSERT INTO specialties (name, uuid)
SELECT 'surgery', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM specialties WHERE name='surgery');

INSERT INTO specialties (name, uuid)
SELECT 'dentistry', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM specialties WHERE name='dentistry');

INSERT INTO vet_specialties (vet_id, specialty_id)
VALUES (2, 1)
ON CONFLICT (vet_id, specialty_id) DO NOTHING;

INSERT INTO vet_specialties (vet_id, specialty_id)
VALUES (3, 2)
ON CONFLICT (vet_id, specialty_id) DO NOTHING;

INSERT INTO vet_specialties (vet_id, specialty_id)
VALUES (3, 3)
ON CONFLICT (vet_id, specialty_id) DO NOTHING;

INSERT INTO vet_specialties (vet_id, specialty_id)
VALUES (4, 2)
ON CONFLICT (vet_id, specialty_id) DO NOTHING;

INSERT INTO vet_specialties (vet_id, specialty_id)
VALUES (5, 1)
ON CONFLICT (vet_id, specialty_id) DO NOTHING;

INSERT INTO types (name, uuid)
SELECT 'cat', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM types WHERE name='cat');

INSERT INTO types (name, uuid)
SELECT 'dog', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM types WHERE name='dog');

INSERT INTO types (name, uuid)
SELECT 'lizard', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM types WHERE name='lizard');

INSERT INTO types (name, uuid)
SELECT 'snake', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM types WHERE name='snake');

INSERT INTO types (name, uuid)
SELECT 'bird', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM types WHERE name='bird');

INSERT INTO types (name, uuid)
SELECT 'hamster', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM types WHERE name='hamster');

INSERT INTO owners (first_name, last_name, address, city, telephone, uuid)
SELECT 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM owners WHERE id=1);

INSERT INTO owners (first_name, last_name, address, city, telephone, uuid)
SELECT 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM owners WHERE id=2);

INSERT INTO owners (first_name, last_name, address, city, telephone, uuid)
SELECT 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM owners WHERE id=3);

INSERT INTO owners (first_name, last_name, address, city, telephone, uuid)
SELECT 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM owners WHERE id=4);

INSERT INTO owners (first_name, last_name, address, city, telephone, uuid)
SELECT 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM owners WHERE id=5);

INSERT INTO owners (first_name, last_name, address, city, telephone, uuid)
SELECT 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM owners WHERE id=6);

INSERT INTO owners (first_name, last_name, address, city, telephone, uuid)
SELECT 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM owners WHERE id=7);

INSERT INTO owners (first_name, last_name, address, city, telephone, uuid)
SELECT 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM owners WHERE id=8);

INSERT INTO owners (first_name, last_name, address, city, telephone, uuid)
SELECT 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM owners WHERE id=9);

INSERT INTO owners (first_name, last_name, address, city, telephone, uuid)
SELECT 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM owners WHERE id=10);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Leo', '2000-09-07', 1, 1, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=1);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Basil', '2002-08-06', 6, 2, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=2);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Rosy', '2001-04-17', 2, 3, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=3);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Jewel', '2000-03-07', 2, 3, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=4);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Iggy', '2000-11-30', 3, 4, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=5);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'George', '2000-01-20', 4, 5, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=6);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Samantha', '1995-09-04', 1, 6, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=7);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Max', '1995-09-04', 1, 6, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=8);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Lucky', '1999-08-06', 5, 7, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=9);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Mulligan', '1997-02-24', 2, 8, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=10);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Freddy', '2000-03-09', 5, 9, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=11);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Lucky', '2000-06-24', 2, 10, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=12);

INSERT INTO pets (name, birth_date, type_id, owner_id, uuid)
SELECT 'Sly', '2002-06-08', 1, 10, gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM pets WHERE id=13);

INSERT INTO visits (pet_id, visit_date, description, uuid)
SELECT 7, '2010-03-04', 'rabies shot', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM visits WHERE id=1);

INSERT INTO visits (pet_id, visit_date, description, uuid)
SELECT 8, '2011-03-04', 'rabies shot', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM visits WHERE id=2);

INSERT INTO visits (pet_id, visit_date, description, uuid)
SELECT 8, '2009-06-04', 'neutered', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM visits WHERE id=3);

INSERT INTO visits (pet_id, visit_date, description, uuid)
SELECT 7, '2008-09-04', 'spayed', gen_random_uuid()
WHERE NOT EXISTS (SELECT * FROM visits WHERE id=4);

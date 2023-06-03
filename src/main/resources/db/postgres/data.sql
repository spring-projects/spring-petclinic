INSERT INTO vets (first_name, last_name) SELECT 'James', 'Carter' WHERE NOT EXISTS (SELECT * FROM vets WHERE first_name='James');
INSERT INTO vets (first_name, last_name) SELECT 'Helen', 'Leary' WHERE NOT EXISTS (SELECT * FROM vets WHERE first_name='Helen');
INSERT INTO vets (first_name, last_name) SELECT 'Linda', 'Douglas' WHERE NOT EXISTS (SELECT * FROM vets WHERE first_name='Linda');
INSERT INTO vets (first_name, last_name) SELECT 'Rafael', 'Ortega' WHERE NOT EXISTS (SELECT * FROM vets WHERE first_name='Rafael');
INSERT INTO vets (first_name, last_name) SELECT 'Henry', 'Stevens' WHERE NOT EXISTS (SELECT * FROM vets WHERE first_name='Henry');
INSERT INTO vets (first_name, last_name) SELECT 'Sharon', 'Jenkins' WHERE NOT EXISTS (SELECT * FROM vets WHERE first_name='Sharon');

INSERT INTO specialties (name) SELECT 'radiology' WHERE NOT EXISTS (SELECT * FROM specialties WHERE name='radiology');
INSERT INTO specialties (name) SELECT 'surgery' WHERE NOT EXISTS (SELECT * FROM specialties WHERE name='surgery'); 
INSERT INTO specialties (name) SELECT 'dentistry' WHERE NOT EXISTS (SELECT * FROM specialties WHERE name='dentistry');

INSERT INTO vet_specialties VALUES ((select id from vets where first_name='Helen'), (select id from specialties where name='radiology')) ON CONFLICT (vet_id, specialty_id) DO NOTHING;
INSERT INTO vet_specialties VALUES ((select id from vets where first_name='Linda'), (select id from specialties where name='surgery')) ON CONFLICT (vet_id, specialty_id) DO NOTHING;
INSERT INTO vet_specialties VALUES ((select id from vets where first_name='Linda'), (select id from specialties where name='dentistry')) ON CONFLICT (vet_id, specialty_id) DO NOTHING;
INSERT INTO vet_specialties VALUES ((select id from vets where first_name='Rafael'), (select id from specialties where name='surgery')) ON CONFLICT (vet_id, specialty_id) DO NOTHING;
INSERT INTO vet_specialties VALUES ((select id from vets where first_name='Henry'), (select id from specialties where name='radiology')) ON CONFLICT (vet_id, specialty_id) DO NOTHING;

INSERT INTO types (name) SELECT 'cat' WHERE NOT EXISTS (SELECT * FROM types WHERE name='cat');
INSERT INTO types (name) SELECT 'dog' WHERE NOT EXISTS (SELECT * FROM types WHERE name='dog');
INSERT INTO types (name) SELECT 'lizard' WHERE NOT EXISTS (SELECT * FROM types WHERE name='lizard');
INSERT INTO types (name) SELECT 'snake' WHERE NOT EXISTS (SELECT * FROM types WHERE name='snake');
INSERT INTO types (name) SELECT 'bird' WHERE NOT EXISTS (SELECT * FROM types WHERE name='bird');
INSERT INTO types (name) SELECT 'hamster' WHERE NOT EXISTS (SELECT * FROM types WHERE name='hamster');

INSERT INTO owners (first_name, last_name, address, city, telephone) SELECT 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023' WHERE NOT EXISTS (SELECT * FROM owners WHERE first_name='George');
INSERT INTO owners (first_name, last_name, address, city, telephone) SELECT 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749' WHERE NOT EXISTS (SELECT * FROM owners WHERE first_name='Betty');
INSERT INTO owners (first_name, last_name, address, city, telephone) SELECT 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763' WHERE NOT EXISTS (SELECT * FROM owners WHERE first_name='Eduardo');
INSERT INTO owners (first_name, last_name, address, city, telephone) SELECT 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198' WHERE NOT EXISTS (SELECT * FROM owners WHERE first_name='Harold');
INSERT INTO owners (first_name, last_name, address, city, telephone) SELECT 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765' WHERE NOT EXISTS (SELECT * FROM owners WHERE first_name='Peter');
INSERT INTO owners (first_name, last_name, address, city, telephone) SELECT 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654' WHERE NOT EXISTS (SELECT * FROM owners WHERE first_name='Jean');
INSERT INTO owners (first_name, last_name, address, city, telephone) SELECT 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387' WHERE NOT EXISTS (SELECT * FROM owners WHERE first_name='Jeff');
INSERT INTO owners (first_name, last_name, address, city, telephone) SELECT 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683' WHERE NOT EXISTS (SELECT * FROM owners WHERE first_name='Maria');
INSERT INTO owners (first_name, last_name, address, city, telephone) SELECT 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435' WHERE NOT EXISTS (SELECT * FROM owners WHERE first_name='David');
INSERT INTO owners (first_name, last_name, address, city, telephone) SELECT 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487' WHERE NOT EXISTS (SELECT * FROM owners WHERE first_name='Carlos');

INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Leo', '2000-09-07', (select id from types where name='cat'), (select id from owners where first_name='George') WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Leo');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Basil', '2002-08-06', (select id from types where name='hamster'), (select id from owners where first_name='Betty') WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Basil');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Rosy', '2001-04-17', (select id from types where name='dog'), (select id from owners where first_name='Eduardo') WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Rosy');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Jewel', '2000-03-07', (select id from types where name='dog'), (select id from owners where first_name='Eduardo') WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Jewel');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Iggy', '2000-11-30', (select id from types where name='lizard'), (select id from owners where first_name='Harold') WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Iggy');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'George', '2000-01-20', (select id from types where name='lizard'), (select id from owners where first_name='Peter') WHERE NOT EXISTS (SELECT * FROM pets WHERE name='George');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Samantha', '1995-09-04', (select id from types where name='snake'), (select id from owners where first_name='Jean') WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Samantha');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Max', '1995-09-04', (select id from types where name='cat'), (select id from owners where first_name='Jean') WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Max');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Lucky', '1999-08-06', (select id from types where name='bird'), (select id from owners where first_name='Jeff') WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Lucky');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Mulligan', '1997-02-24', (select id from types where name='dog'), (select id from owners where first_name='Maria')  WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Mulligan');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Freddy', '2000-03-09', (select id from types where name='bird'), (select id from owners where first_name='David')  WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Freddy');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Lucky', '2000-06-24', (select id from types where name='dog'), (select id from owners where first_name='Carlos') WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Lucky');
INSERT INTO pets (name, birth_date, type_id, owner_id) SELECT 'Sly', '2002-06-08', (select id from types where name='cat'), (select id from owners where first_name='Carlos')  WHERE NOT EXISTS (SELECT * FROM pets WHERE name='Sly''');

INSERT INTO visits (pet_id, visit_date, description) SELECT (select id from pets where name='Samantha'), '2010-03-04', 'rabies shot' WHERE NOT EXISTS (SELECT * FROM visits WHERE visit_date='2010-03-04');
INSERT INTO visits (pet_id, visit_date, description) SELECT (select id from pets where name='Max'), '2011-03-04', 'rabies shot' WHERE NOT EXISTS (SELECT * FROM visits WHERE visit_date='2011-03-04');
INSERT INTO visits (pet_id, visit_date, description) SELECT (select id from pets where name='Max'), '2009-06-04', 'neutered' WHERE NOT EXISTS (SELECT * FROM visits WHERE visit_date='2009-06-04');
INSERT INTO visits (pet_id, visit_date, description) SELECT (select id from pets where name='Samantha'), '2008-09-04', 'spayed' WHERE NOT EXISTS (SELECT * FROM visits WHERE visit_date='2008-09-04');

INSERT IGNORE INTO specialties (id, name, uuid) VALUES (1, 'radiology', '0a6d7430-1234-4c3a-bc34-65b0ef2f7d48');
INSERT IGNORE INTO specialties (id, name, uuid) VALUES (2, 'surgery', 'e3a10a00-4567-4c98-89ba-d7bb3e9a999a');
INSERT IGNORE INTO specialties (id, name, uuid) VALUES (3, 'dentistry', '1a78df21-5678-4b5a-ae13-88f72065b4b1');

INSERT IGNORE INTO vet_specialties (vet_id, specialty_id) VALUES (2, 1);
INSERT IGNORE INTO vet_specialties (vet_id, specialty_id) VALUES (3, 2);
INSERT IGNORE INTO vet_specialties (vet_id, specialty_id) VALUES (3, 3);
INSERT IGNORE INTO vet_specialties (vet_id, specialty_id) VALUES (4, 2);
INSERT IGNORE INTO vet_specialties (vet_id, specialty_id) VALUES (5, 1);

INSERT IGNORE INTO types (id, name, uuid) VALUES (1, 'cat', '9a7b1789-5678-4c23-b90c-ef3f82c3bcde');
INSERT IGNORE INTO types (id, name, uuid) VALUES (2, 'dog', '3b8a2a12-4567-4c56-bb12-d3b50efc23b2');
INSERT IGNORE INTO types (id, name, uuid) VALUES (3, 'lizard', '5d04f9ac-3456-4c68-8f10-0c5d8e91f6b3');
INSERT IGNORE INTO types (id, name, uuid) VALUES (4, 'snake', 'fa0d4bce-6789-4c9a-b12f-df2b34d5e678');
INSERT IGNORE INTO types (id, name, uuid) VALUES (5, 'bird', '7c4e2a79-1234-4a23-bc12-f7e8a6e2c9a1');
INSERT IGNORE INTO types (id, name, uuid) VALUES (6, 'hamster', 'b0f9a8a7-6789-4e6f-9c10-c45d1c8d567a');

INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone, uuid) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', '0bc6a123-8c4e-4e2b-bc45-7a3e1d5e0236');
INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone, uuid) VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'abc4e572-8a4d-41d1-bc67-d7c4e2c3b123');
INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone, uuid) VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'de5f4b78-7a9b-4c12-8a3f-8e2c34d6c4b5');
INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone, uuid) VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', '2a4d9b89-1234-4a56-8c4f-7a5d2b3d6c2e');
INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone, uuid) VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'bc7f9a9a-5678-4e3d-8c5a-3b9e7a5f1234');
INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone, uuid) VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', '567f1234-1234-4b6a-8d5e-3a5f9e8b6a2b');
INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone, uuid) VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', '3b5a9f34-3456-4567-8a23-d5e7a12c3f67');
INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone, uuid) VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'a2d3c456-4b56-4567-8a34-d4e7f23b6a7e');
INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone, uuid) VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'de34a124-4e3f-4c2d-b123-8a4d1c7e8a7b');
INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone, uuid) VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', '5f6b7a5c-8b9e-4f23-8d67-d5e7c2c3a5d9');

INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (1, 'Leo', '2000-09-07', 1, 1, '67a5d89a-9b12-4a23-9f23-d7e8c6a5b12f');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (2, 'Basil', '2002-08-06', 6, 2, '1b7a5d9a-4e7f-4a23-8b9f-7a3e6b2d8e7f');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (3, 'Rosy', '2001-04-17', 2, 3, '5b7a4f9a-5678-4c2d-9f45-7e5d1f23b12f');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (4, 'Jewel', '2000-03-07', 2, 3, '6b7a4c9d-8b9e-4c12-9d34-e7a5f9b12c34');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (5, 'Iggy', '2000-11-30', 3, 4, 'de34b2f9-4567-4a23-9f6e-d3e7a5d6c7a9');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (6, 'George', '2000-01-20', 4, 5, '67a7d3f9-4c6e-4d12-8f9b-9c3d8e5f7a2d');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (7, 'Samantha', '1995-09-04', 1, 6, '9a5b7e34-4d67-4a12-bc23-8e5f4d7b3c2d');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (8, 'Max', '1995-09-04', 1, 6, '1a9d3e5b-1234-4d23-8b7e-c5d3a8e2b5d7');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (9, 'Lucky', '1999-08-06', 5, 7, '5a7d9e2f-9c8e-4f23-9b67-c7a3e5d9b123');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (10, 'Mulligan', '1997-02-24', 2, 8, '123b4e67-6789-4f12-9c5e-9d3b7a5e2c8d');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (11, 'Freddy', '2000-03-09', 5, 9, '7c5d1234-4a67-4f12-8c67-8e3f5a6b9d2e');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (12, 'Lucky', '2000-06-24', 2, 10, '67c5a12e-4a9e-4b6f-8d23-9e5f7b3c5d8e');
INSERT IGNORE INTO pets (id, name, birth_date, type_id, owner_id, uuid) VALUES (13, 'Sly', '2002-06-08', 1, 10, '9a6b7f34-5678-4c2d-9f3e-5a7d1b6c4e23');

INSERT IGNORE INTO visits (id, pet_id, visit_date, description, uuid) VALUES (1, 7, '2010-03-04', 'rabies shot', '7a9e1b34-4f12-4e2b-8a5d-9f2c6a7b9d3e');
INSERT IGNORE INTO visits (id, pet_id, visit_date, description, uuid) VALUES (2, 8, '2011-03-04', 'rabies shot', '67a5b9e1-4c56-4e2d-9b6f-c7a5d8e4f3d7');
INSERT IGNORE INTO visits (id, pet_id, visit_date, description, uuid) VALUES (3, 8, '2009-06-04', 'neutered', '5c7a1e34-8b9e-4d2f-9c56-5e2f7d8b9d6c');
INSERT IGNORE INTO visits (id, pet_id, visit_date, description, uuid) VALUES (4, 7, '2008-09-04', 'spayed', '9a5d7b12-8e34-4f23-9b6f-8c3d1a6f7b2e');

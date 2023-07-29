INSERT INTO vets(FIRST_NAME,LAST_NAME) VALUES ('James', 'Carter');
INSERT INTO vets(FIRST_NAME,LAST_NAME) VALUES ('Helen', 'Leary');
INSERT INTO vets(FIRST_NAME,LAST_NAME) VALUES ('Linda', 'Douglas');
INSERT INTO vets(FIRST_NAME,LAST_NAME) VALUES ('Rafael', 'Ortega');
INSERT INTO vets(FIRST_NAME,LAST_NAME) VALUES ('Henry', 'Stevens');
INSERT INTO vets(FIRST_NAME,LAST_NAME) VALUES ('Sharon', 'Jenkins');

INSERT INTO specialties(NAME) VALUES ('radiology');
INSERT INTO specialties(NAME) VALUES ('surgery');
INSERT INTO specialties(NAME) VALUES ('dentistry');

INSERT INTO types(NAME) VALUES ('cat');
INSERT INTO types(NAME) VALUES ('dog');
INSERT INTO types(NAME) VALUES ('lizard');
INSERT INTO types(NAME) VALUES ('snake');
INSERT INTO types(NAME) VALUES ('bird');
INSERT INTO types(NAME) VALUES ('hamster');

INSERT INTO owners(FIRST_NAME,LAST_NAME,ADDRESS,CITY,TELEPHONE) VALUES ('George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT INTO owners(FIRST_NAME,LAST_NAME,ADDRESS,CITY,TELEPHONE) VALUES ('Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT INTO owners(FIRST_NAME,LAST_NAME,ADDRESS,CITY,TELEPHONE) VALUES ('Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT INTO owners(FIRST_NAME,LAST_NAME,ADDRESS,CITY,TELEPHONE) VALUES ('Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT INTO owners(FIRST_NAME,LAST_NAME,ADDRESS,CITY,TELEPHONE) VALUES ('Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT INTO owners(FIRST_NAME,LAST_NAME,ADDRESS,CITY,TELEPHONE) VALUES ('Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT INTO owners(FIRST_NAME,LAST_NAME,ADDRESS,CITY,TELEPHONE) VALUES ('Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT INTO owners(FIRST_NAME,LAST_NAME,ADDRESS,CITY,TELEPHONE) VALUES ('Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT INTO owners(FIRST_NAME,LAST_NAME,ADDRESS,CITY,TELEPHONE) VALUES ('David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT INTO owners(FIRST_NAME,LAST_NAME,ADDRESS,CITY,TELEPHONE) VALUES ('Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT INTO vet_specialties(VET_ID,SPECIALTY_ID) VALUES (2, 1);
INSERT INTO vet_specialties(VET_ID,SPECIALTY_ID) VALUES (3, 2);
INSERT INTO vet_specialties(VET_ID,SPECIALTY_ID) VALUES (3, 3);
INSERT INTO vet_specialties(VET_ID,SPECIALTY_ID) VALUES (4, 2);
INSERT INTO vet_specialties(VET_ID,SPECIALTY_ID) VALUES (5, 1);

INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Leo', to_date('2010-09-07', 'yyyy-mm-dd'), 1, 1);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Basil', to_date('2012-08-06', 'yyyy-mm-dd'), 6, 2);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Rosy', to_date('2011-04-17', 'yyyy-mm-dd'), 2, 3);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Jewel', to_date('2010-03-07', 'yyyy-mm-dd'), 2, 3);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Iggy', to_date('2010-11-30', 'yyyy-mm-dd'), 3, 4);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('George', to_date('2010-01-20', 'yyyy-mm-dd'), 4, 5);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Samantha', to_date('2012-09-04', 'yyyy-mm-dd'), 1, 6);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Max', to_date('2012-09-04', 'yyyy-mm-dd'), 1, 6);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Lucky', to_date('2011-08-06', 'yyyy-mm-dd'), 5, 7);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Mulligan', to_date('2007-02-24', 'yyyy-mm-dd'), 2, 8);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Freddy', to_date('2010-03-09', 'yyyy-mm-dd'), 5, 9);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Lucky', to_date('2010-06-24', 'yyyy-mm-dd'), 2, 10);
INSERT INTO pets(NAME,BIRTH_DATE,TYPE_ID,OWNER_ID) VALUES ('Sly', to_date('2012-06-08', 'yyyy-mm-dd'), 1, 10);

INSERT INTO visits(PET_ID,VISIT_DATE,DESCRIPTION) VALUES (7, to_date('2013-01-01', 'yyyy-mm-dd'), 'rabies shot');
INSERT INTO visits(PET_ID,VISIT_DATE,DESCRIPTION) VALUES (8, to_date('2013-01-02', 'yyyy-mm-dd'), 'rabies shot');
INSERT INTO visits(PET_ID,VISIT_DATE,DESCRIPTION) VALUES (8, to_date('2013-01-03', 'yyyy-mm-dd'), 'neutered');
INSERT INTO visits(PET_ID,VISIT_DATE,DESCRIPTION) VALUES (7, to_date('2013-01-04', 'yyyy-mm-dd'), 'spayed');
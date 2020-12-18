INSERT IGNORE INTO vets VALUES (1, 'James', 'Carter');
INSERT IGNORE INTO vets VALUES (2, 'Helen', 'Leary');
INSERT IGNORE INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT IGNORE INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT IGNORE INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT IGNORE INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT IGNORE INTO specialties VALUES (1, 'radiology');
INSERT IGNORE INTO specialties VALUES (2, 'surgery');
INSERT IGNORE INTO specialties VALUES (3, 'dentistry');

INSERT IGNORE INTO vet_specialties VALUES (2, 1);
INSERT IGNORE INTO vet_specialties VALUES (3, 2);
INSERT IGNORE INTO vet_specialties VALUES (3, 3);
INSERT IGNORE INTO vet_specialties VALUES (4, 2);
INSERT IGNORE INTO vet_specialties VALUES (5, 1);

INSERT IGNORE INTO types VALUES (1, 'cat');
INSERT IGNORE INTO types VALUES (2, 'dog');
INSERT IGNORE INTO types VALUES (3, 'lizard');
INSERT IGNORE INTO types VALUES (4, 'snake');
INSERT IGNORE INTO types VALUES (5, 'bird');
INSERT IGNORE INTO types VALUES (6, 'hamster');

INSERT IGNORE INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT IGNORE INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT IGNORE INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT IGNORE INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT IGNORE INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT IGNORE INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT IGNORE INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT IGNORE INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT IGNORE INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT IGNORE INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT IGNORE INTO pets VALUES (1, 'Leo', '2000-09-07', 1, 1);
INSERT IGNORE INTO pets VALUES (2, 'Basil', '2002-08-06', 6, 2);
INSERT IGNORE INTO pets VALUES (3, 'Rosy', '2001-04-17', 2, 3);
INSERT IGNORE INTO pets VALUES (4, 'Jewel', '2000-03-07', 2, 3);
INSERT IGNORE INTO pets VALUES (5, 'Iggy', '2000-11-30', 3, 4);
INSERT IGNORE INTO pets VALUES (6, 'George', '2000-01-20', 4, 5);
INSERT IGNORE INTO pets VALUES (7, 'Samantha', '1995-09-04', 1, 6);
INSERT IGNORE INTO pets VALUES (8, 'Max', '1995-09-04', 1, 6);
INSERT IGNORE INTO pets VALUES (9, 'Lucky', '1999-08-06', 5, 7);
INSERT IGNORE INTO pets VALUES (10, 'Mulligan', '1997-02-24', 2, 8);
INSERT IGNORE INTO pets VALUES (11, 'Freddy', '2000-03-09', 5, 9);
INSERT IGNORE INTO pets VALUES (12, 'Lucky', '2000-06-24', 2, 10);
INSERT IGNORE INTO pets VALUES (13, 'Sly', '2002-06-08', 1, 10);

INSERT IGNORE INTO visits VALUES (1, 7, '2010-03-04', 'rabies shot');
INSERT IGNORE INTO visits VALUES (2, 8, '2011-03-04', 'rabies shot');
INSERT IGNORE INTO visits VALUES (3, 8, '2009-06-04', 'neutered');
INSERT IGNORE INTO visits VALUES (4, 7, '2008-09-04', 'spayed');

INSERT INTO roles (id, name) VALUES
  (1,'ROLE_ADMIN'),
  (2,'ROLE_STAFF'),
  (3,'ROLE_USER');

INSERT INTO privileges (id, name) VALUES
  (1,'CREATE'),
  (2,'READ'),
  (3,'UPDATE'),
  (4,'DELETE');

INSERT INTO users (id, first_name, last_name, email, password, enabled, telephone, street1, zip_code, city, country) VALUES
  (1, 'George', 'Franklin', 'georges.franklin@petclinic.com', '$2a$10$8KypNYtPopFo8Sk5jbKJ4.lCKeBhdApsrkmFfhwjB8nCls8qpzjZG', true, '6085551023', '110 W. Liberty St.',12354,'Madison','USA'),
  (2, 'Betty', 'Davis', 'betty.davis@petclinic.com', '$2a$10$InKx/fhX3CmLi8zKpHYx/.ETHUlZwvT1xn.Za/pp2JR0iEtYV9a9O', true, '6085551749','638 Cardinal Ave.', 6546, 'Sun Prairie', 'USA'),
  (3, 'Eduardo', 'Rodriquez', 'eduardo.rodriguez@petclinic.com', '$2a$10$P55nbvVibHpoyWzenHngjOf.oEmcj74mI/VJaUZwGX9v8klctzsNW', true, '6085558763','2693 Commerce St.', 65454, 'McFarland', 'USA'),
  (4, 'Paul-Emmanuel','DOS SANTOS FACAO','pedsf.fullstack@gmail.com','$2a$10$AzoUxi1IQFJMzLHcCGmDjuDHAQqAcAiRLz6UMeItdTL3mMWxMZEPC', true, '6085558763','2693 Commerce St.', 65454, 'McFarland', 'USA');

INSERT INTO users_roles (user_id, role_id) VALUES
  (1,1),(1,2),(1,3),
  (2,3),(3,3);

INSERT INTO roles_privileges (role_id, privilege_id) values
  (1,1),(1,2),(1,3),(1,4),
  (2,1),(2,2),(2,3),
  (3,1),(3,2);

INSERT INTO auth_providers (id, name) VALUES
  (1,'local'),
  (2,'google'),
  (3,'github'),
  (4,'facebook');

INSERT INTO credentials (provider_id, email, password, verified) VALUES
  (1, 'georges.franklin@petclinic.com', '$2a$10$8KypNYtPopFo8Sk5jbKJ4.lCKeBhdApsrkmFfhwjB8nCls8qpzjZG', true),
  (1, 'betty.davis@petclinic.com', '$2a$10$InKx/fhX3CmLi8zKpHYx/.ETHUlZwvT1xn.Za/pp2JR0iEtYV9a9O', true),
  (1, 'eduardo.rodriguez@petclinic.com', '$2a$10$P55nbvVibHpoyWzenHngjOf.oEmcj74mI/VJaUZwGX9v8klctzsNW', true),
  (2, 'pedsf.fullstack@gmail.com','117496521794255275093', true);


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


-- Sample data for feature flags

-- 1. SIMPLE flag: Add New Pet (enabled by default)
INSERT IGNORE INTO feature_flags (flag_key, description, flag_type, enabled, percentage, created_at, updated_at)
VALUES ('ADD_NEW_PET', 'Controls whether users can add new pets to an owner', 'SIMPLE', TRUE, NULL, NOW(), NOW());

-- 2. SIMPLE flag: Add Visit (enabled by default)
INSERT IGNORE INTO feature_flags (flag_key, description, flag_type, enabled, percentage, created_at, updated_at)
VALUES ('ADD_VISIT', 'Controls whether users can add new visits for pets', 'SIMPLE', TRUE, NULL, NOW(), NOW());

-- 3. WHITELIST flag: Owner Search (only specific users can search)
INSERT IGNORE  INTO feature_flags (flag_key, description, flag_type, enabled, percentage, created_at, updated_at)
VALUES ('OWNER_SEARCH', 'Controls who can search for owners', 'SIMPLE', TRUE, NULL, NOW(), NOW());

-- Add whitelist items for owner-search (example user contexts)
-- INSERT IGNORE INTO feature_flag_whitelist (feature_flag_id, whitelist)
-- SELECT id, 'admin' FROM feature_flags WHERE flag_key = 'OWNER_SEARCH';

-- INSERT IGNORE INTO feature_flag_whitelist (feature_flag_id, whitelist)
-- SELECT id, 'Ramprakash' FROM feature_flags WHERE flag_key = 'OWNER_SEARCH';

-- 4. PERCENTAGE flag: New UI Theme (gradually roll out to 50% of users)
INSERT IGNORE INTO feature_flags (flag_key, description, flag_type, enabled, percentage, created_at, updated_at)
VALUES ('NEW_UI_THEME', 'Gradually roll out new UI theme', 'PERCENTAGE', TRUE, 50, NOW(), NOW());

-- 5. BLACKLIST flag: Delete Owner (block specific users from deleting)
INSERT IGNORE INTO feature_flags (flag_key, description, flag_type, enabled, percentage, created_at, updated_at)
VALUES ('DELETE_OWNER', 'Controls who can delete owners', 'BLACKLIST', TRUE, NULL, NOW(), NOW());

-- Add blacklist items
INSERT IGNORE INTO feature_flag_blacklist (feature_flag_id, blacklist)
SELECT id, 'guest' FROM feature_flags WHERE flag_key = 'DELETE_OWNER';

INSERT IGNORE INTO feature_flag_blacklist (feature_flag_id, blacklist)
SELECT id, 'readonly_user' FROM feature_flags WHERE flag_key = 'DELETE_OWNER';

-- 6. GLOBAL_DISABLE flag: Emergency shutdown example
INSERT IGNORE INTO feature_flags (flag_key, description, flag_type, enabled, percentage, created_at, updated_at)
VALUES ('EMERGENCY_SHUTDOWN', 'Emergency feature kill switch', 'GLOBAL_DISABLE', FALSE, NULL, NOW(), NOW());
CREATE TABLE IF NOT EXISTS vets (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  INDEX(last_name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS specialties (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80),
  INDEX(name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS vet_specialties (
  vet_id INT(4) UNSIGNED NOT NULL,
  specialty_id INT(4) UNSIGNED NOT NULL,
  FOREIGN KEY (vet_id) REFERENCES vets(id),
  FOREIGN KEY (specialty_id) REFERENCES specialties(id),
  UNIQUE (vet_id,specialty_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS types (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80),
  INDEX(name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS owners (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  address VARCHAR(255),
  city VARCHAR(80),
  telephone VARCHAR(20),
  INDEX(last_name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS pets (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(30),
  birth_date DATE,
  type_id INT(4) UNSIGNED NOT NULL,
  owner_id INT(4) UNSIGNED NOT NULL,
  INDEX(name),
  FOREIGN KEY (owner_id) REFERENCES owners(id),
  FOREIGN KEY (type_id) REFERENCES types(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS visits (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  pet_id INT(4) UNSIGNED NOT NULL,
  visit_date DATE,
  description VARCHAR(255),
  FOREIGN KEY (pet_id) REFERENCES pets(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS roles (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(20) NOT NULL,
  INDEX(name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS privileges (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(20) NOT NULL,
  INDEX(name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS users (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR_IGNORECASE(30) NOT NULL,
  email      VARCHAR(50) NOT NULL,
  password   VARCHAR(255) NOT NULL,
  enabled    BOOLEAN NOT NULL,
  account_unexpired    BOOLEAN NOT NULL DEFAULT true,
  account_unlocked     BOOLEAN NOT NULL DEFAULT true,
  credential_unexpired BOOLEAN NOT NULL DEFAULT true,
  telephone  VARCHAR(20),
  street1    VARCHAR(50),
  street2    VARCHAR(50),
  street3    VARCHAR(50),
  zip_code   VARCHAR(6),
  city       VARCHAR(80),
  country    VARCHAR(50),
  INDEX(email)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS users_roles (
  user_id INTEGER NOT NULL,
  role_id INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id),
  INDEX(user_role)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS roles_privileges (
  role_id INTEGER NOT NULL,
  privilege_id INTEGER NOT NULL,
  FOREIGN KEY (role_id) REFERENCES roles(id),
  FOREIGN KEY (privilege_id) REFERENCES privileges(id),
  INDEX(role_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS auth_providers (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(20) NOT NULL,
  INDEX(name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS credentials (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  provider_id INTEGER NOT NULL,
  email       VARCHAR(50) NOT NULL,
  password    VARCHAR(255) NOT NULL,
  verified    BOOLEAN NOT NULL,
  token       VARCHAR(255) DEFAULT NULL,
  expiration  DATE DEFAULT NULL,
  FOREIGN KEY (provider_id) REFERENCES auth_providers(id),
  INDEX(email)
) engine=InnoDB;

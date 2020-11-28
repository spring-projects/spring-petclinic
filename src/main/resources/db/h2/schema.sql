


DROP TABLE vets IF EXISTS;
CREATE TABLE vets (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30)
);
CREATE INDEX vets_last_name ON vets (last_name);

DROP TABLE specialties IF EXISTS;
CREATE TABLE specialties (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX specialties_name ON specialties (name);

DROP TABLE vet_specialties IF EXISTS;
CREATE TABLE vet_specialties (
  vet_id       INTEGER NOT NULL,
  specialty_id INTEGER NOT NULL
);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_vets FOREIGN KEY (vet_id) REFERENCES vets (id);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id);

DROP TABLE types IF EXISTS;
CREATE TABLE types (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX types_name ON types (name);

DROP TABLE owners IF EXISTS;
CREATE TABLE owners (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR_IGNORECASE(30),
  address    VARCHAR(255),
  city       VARCHAR(80),
  telephone  VARCHAR(20)
);
CREATE INDEX owners_last_name ON owners (last_name);

DROP TABLE pets IF EXISTS;
CREATE TABLE pets (
  id         INTEGER IDENTITY PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  type_id    INTEGER NOT NULL,
  owner_id   INTEGER NOT NULL
);
ALTER TABLE pets ADD CONSTRAINT fk_pets_owners FOREIGN KEY (owner_id) REFERENCES owners (id);
ALTER TABLE pets ADD CONSTRAINT fk_pets_types FOREIGN KEY (type_id) REFERENCES types (id);
CREATE INDEX pets_name ON pets (name);

DROP TABLE visits IF EXISTS;
CREATE TABLE visits (
  id          INTEGER IDENTITY PRIMARY KEY,
  pet_id      INTEGER NOT NULL,
  visit_date  DATE,
  description VARCHAR(255)
);
ALTER TABLE visits ADD CONSTRAINT fk_visits_pets FOREIGN KEY (pet_id) REFERENCES pets (id);
CREATE INDEX visits_pet_id ON visits (pet_id);

DROP TABLE roles IF EXISTS;
CREATE TABLE roles (
  id          INTEGER IDENTITY PRIMARY KEY,
  name        VARCHAR(20) NOT NULL
);
CREATE INDEX roles_name ON roles (name);

DROP TABLE privileges IF EXISTS;
CREATE TABLE privileges (
  id          INTEGER IDENTITY PRIMARY KEY,
  name        VARCHAR(20) NOT NULL
);
CREATE INDEX privileges_name ON privileges (name);


DROP TABLE users IF EXISTS;
CREATE TABLE users (
  id         INTEGER IDENTITY PRIMARY KEY,
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
  country    VARCHAR(50)
);
CREATE INDEX users_email ON users (email);

DROP TABLE users_roles IF EXISTS;
CREATE TABLE users_roles (
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL
);
ALTER TABLE users_roles ADD CONSTRAINT fk_users_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE users_roles ADD CONSTRAINT fk_users_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id);
CREATE INDEX users_roles_user_id ON users_roles (user_id);

DROP TABLE roles_privileges IF EXISTS;
CREATE TABLE roles_privileges (
    role_id INTEGER NOT NULL,
    privilege_id INTEGER NOT NULL
);


DROP TABLE auth_providers IF EXISTS;
CREATE TABLE auth_providers (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(20) NOT NULL
);
CREATE INDEX auth_providers_name ON auth_providers (name);

DROP TABLE credentials IF EXISTS;
CREATE TABLE credentials (
  id          INTEGER IDENTITY PRIMARY KEY,
  provider_id INTEGER NOT NULL,
  email       VARCHAR(50) NOT NULL,
  password    VARCHAR(255) NOT NULL,
  verified    BOOLEAN NOT NULL,
  token       VARCHAR(255) DEFAULT NULL,
  expiration  DATE DEFAULT NULL
);
ALTER TABLE credentials ADD CONSTRAINT fk_credentials_provider_id FOREIGN KEY (provider_id) REFERENCES auth_providers (id);
CREATE INDEX credentials_email ON credentials (email);

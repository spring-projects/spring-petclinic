DROP TABLE vet_specialties IF EXISTS;
DROP TABLE vets IF EXISTS;
DROP TABLE specialties IF EXISTS;
DROP TABLE visits IF EXISTS;
DROP TABLE pets IF EXISTS;
DROP TABLE types IF EXISTS;
DROP TABLE owners IF EXISTS;
DROP TABLE roles IF EXISTS;
DROP TABLE users IF EXISTS;


CREATE TABLE vets (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30)
);
CREATE INDEX vets_last_name ON vets (last_name);

CREATE TABLE specialties (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX specialties_name ON specialties (name);

CREATE TABLE vet_specialties (
  vet_id       INTEGER NOT NULL,
  specialty_id INTEGER NOT NULL
);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_vets FOREIGN KEY (vet_id) REFERENCES vets (id);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id);

CREATE TABLE types (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX types_name ON types (name);

CREATE TABLE owners (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR_IGNORECASE(30),
  address    VARCHAR(255),
  city       VARCHAR(80),
  telephone  VARCHAR(20)
);
CREATE INDEX owners_last_name ON owners (last_name);

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

CREATE TABLE visits (
  id          INTEGER IDENTITY PRIMARY KEY,
  pet_id      INTEGER NOT NULL,
  visit_date  DATE,
  description VARCHAR(255)
);
ALTER TABLE visits ADD CONSTRAINT fk_visits_pets FOREIGN KEY (pet_id) REFERENCES pets (id);
CREATE INDEX visits_pet_id ON visits (pet_id);

CREATE TABLE roles (
  id          INTEGER IDENTITY PRIMARY KEY,
  name        VARCHAR(20) NOT NULL
);
CREATE INDEX roles_name ON roles (name);

CREATE TABLE users (
  id              INTEGER IDENTITY PRIMARY KEY,
  first_name      VARCHAR(30) NOT NULL,
  last_name       VARCHAR_IGNORECASE(30) NOT NULL,
  email           VARCHAR(50) NOT NULL,
  email_verified  BOOLEAN NOT NULL,
  password        VARCHAR(255) NOT NULL,
  provider        VARCHAR(20),
  provider_id     VARCHAR(20),
  telephone       VARCHAR(20),
  street1         VARCHAR(50),
  street2         VARCHAR(50),
  street3         VARCHAR(50),
  zip_code        VARCHAR(6),
  city            VARCHAR(80),
  country         VARCHAR(50)
);
CREATE INDEX users_email ON users (email);

CREATE TABLE public.users_roles (
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL
);
ALTER TABLE users_roles ADD CONSTRAINT fk_users_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE users_roles ADD CONSTRAINT fk_users_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id);
CREATE INDEX users_roles_user_id ON users_roles (user_id);

DROP TABLE doctor_specialties IF EXISTS;
DROP TABLE doctors IF EXISTS;
DROP TABLE specialties IF EXISTS;
DROP TABLE visits IF EXISTS;
DROP TABLE kids IF EXISTS;
DROP TABLE gender IF EXISTS;
DROP TABLE parents IF EXISTS;


CREATE TABLE doctors (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30)
);
CREATE INDEX doctors_last_name ON doctors (last_name);

CREATE TABLE specialties (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX specialties_name ON specialties (name);

CREATE TABLE doctor_specialties (
  doctor_id       INTEGER NOT NULL,
  specialty_id INTEGER NOT NULL
);
ALTER TABLE doctor_specialties ADD CONSTRAINT fk_doctor_specialties_doctors FOREIGN KEY (doctor_id) REFERENCES doctors (id);
ALTER TABLE doctor_specialties ADD CONSTRAINT fk_doctor_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id);

CREATE TABLE gender (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX gender_name ON gender (name);

CREATE TABLE parents (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR_IGNORECASE(30),
  address    VARCHAR(255),
  city       VARCHAR(80),
  telephone  VARCHAR(20)
);
CREATE INDEX parents_last_name ON parents (last_name);

CREATE TABLE kids (
  id         INTEGER IDENTITY PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  gender_id    INTEGER NOT NULL,
  parent_id   INTEGER NOT NULL
);
ALTER TABLE kids ADD CONSTRAINT fk_kids_parents FOREIGN KEY (parent_id) REFERENCES parents (id);
ALTER TABLE kids ADD CONSTRAINT fk_kids_gender FOREIGN KEY (gender_id) REFERENCES gender (id);
CREATE INDEX kids_name ON kids (name);

CREATE TABLE visits (
  id          INTEGER IDENTITY PRIMARY KEY,
  kid_id      INTEGER NOT NULL,
  visit_date  DATE,
  description VARCHAR(255)
);
ALTER TABLE visits ADD CONSTRAINT fk_visits_kids FOREIGN KEY (kid_id) REFERENCES kids (id);
CREATE INDEX visits_kid_id ON visits (kid_id);

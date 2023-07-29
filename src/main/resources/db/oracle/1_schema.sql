CREATE table petclinic.vets (
       id number(10,0) generated as identity,
        first_name varchar2(255 char),
        last_name varchar2(255 char),
        primary key (id)
);
CREATE INDEX vets_last_name ON vets(last_name);

CREATE table specialties (
       id number(10,0) generated as identity,
        name varchar2(255 char),
        primary key (id)
);
CREATE INDEX specialties_name ON specialties (name);

CREATE table vet_specialties (
       vet_id number(10,0) not null,
        specialty_id number(10,0) not null,
        primary key (vet_id, specialty_id)
);

CREATE table types (
       id number(10,0) generated as identity,
        name varchar2(255 char),
        primary key (id)
);
CREATE INDEX types_name ON types (name);

CREATE table owners (
       id number(10,0) generated as identity,
        first_name varchar2(255 char),
        last_name varchar2(255 char),
        address varchar2(255 char),
        city varchar2(255 char),
        telephone varchar2(255 char),
        primary key (id)
);
CREATE INDEX owners_last_name ON owners (last_name);

CREATE table pets (
       id number(10,0) generated as identity,
        name varchar2(255 char),
        birth_date date,
        owner_id number(10,0),
        type_id number(10,0),
        primary key (id)
);
CREATE INDEX pets_name ON pets(name);

CREATE table visits (
       id number(10,0) generated as identity,
        visit_date date,
        description varchar2(255 char),
        pet_id number(10,0),
        primary key (id)
);
CREATE INDEX visits_pet_id ON visits (pet_id);


ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_vets FOREIGN KEY (vet_id) REFERENCES vets (id);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id);
ALTER TABLE pets ADD CONSTRAINT fk_pets_owners FOREIGN KEY (owner_id) REFERENCES owners (id);
ALTER TABLE pets ADD CONSTRAINT fk_pets_types FOREIGN KEY (type_id) REFERENCES types (id);
ALTER TABLE visits ADD CONSTRAINT fk_visits_pets FOREIGN KEY (pet_id) REFERENCES pets (id);
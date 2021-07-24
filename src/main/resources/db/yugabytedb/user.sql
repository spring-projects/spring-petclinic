DROP DATABASE IF EXISTS "petclinic";
DROP USER IF EXISTS "petclinic";
CREATE DATABASE "petclinic";
CREATE USER "petclinic" WITH PASSWORD 'petclinic';
GRANT ALL PRIVILEGES ON DATABASE "petclinic" to "petclinic";

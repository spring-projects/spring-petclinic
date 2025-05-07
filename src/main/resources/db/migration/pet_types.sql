CREATE TABLE pet_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    temperament VARCHAR(255),
    length DOUBLE,
    weight DOUBLE,
);

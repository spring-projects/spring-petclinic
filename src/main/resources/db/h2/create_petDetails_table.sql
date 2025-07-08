CREATE TABLE pet_types (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL
);

CREATE TABLE pets (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    birth_date DATE,
    type_id INT,
    FOREIGN KEY (type_id) REFERENCES pet_types(id)
);

CREATE TABLE visits (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255),
    FOREIGN KEY (pet_id) REFERENCES pets(id)
);

CREATE TABLE pet_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pet_id INT NOT NULL UNIQUE,
    temperament VARCHAR(100),
    weight DOUBLE,
    length DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pet_id) REFERENCES pets(id)
);

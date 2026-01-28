CREATE TABLE IF NOT EXISTS appointments (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    pet_id INT NOT NULL,
    owner_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    version INT NOT NULL,
    FOREIGN KEY (pet_id) REFERENCES pets(id),
    FOREIGN KEY (owner_id) REFERENCES owners(id)
);

CREATE INDEX IF NOT EXISTS idx_appointments_pet_id ON appointments(pet_id);
CREATE INDEX IF NOT EXISTS idx_appointments_start_time ON appointments(start_time);
CREATE INDEX IF NOT EXISTS idx_appointments_end_time ON appointments(end_time);
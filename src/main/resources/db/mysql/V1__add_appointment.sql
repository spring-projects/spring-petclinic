-- Create appointments table
CREATE TABLE IF NOT EXISTS appointments (
  id          INTEGER AUTO_INCREMENT PRIMARY KEY,
  pet_id      INTEGER NOT NULL,
  owner_id    INTEGER NOT NULL,
  start_time  DATETIME NOT NULL,
  end_time    DATETIME NOT NULL,
  status      VARCHAR(20) NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  version     INTEGER DEFAULT 0,
  CONSTRAINT fk_appointments_pets FOREIGN KEY (pet_id) REFERENCES pets (id),
  CONSTRAINT fk_appointments_owners FOREIGN KEY (owner_id) REFERENCES owners (id),
  CONSTRAINT chk_appointment_time_order CHECK (end_time > start_time),
  INDEX idx_appointments_pet_id (pet_id),
  INDEX idx_appointments_start_time (start_time),
  INDEX idx_appointments_pet_time_range (pet_id, start_time, end_time)
);
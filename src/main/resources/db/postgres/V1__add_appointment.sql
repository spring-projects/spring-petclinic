-- Create appointments table
CREATE TABLE IF NOT EXISTS appointments (
  id          SERIAL PRIMARY KEY,
  pet_id      INTEGER NOT NULL,
  owner_id    INTEGER NOT NULL,
  start_time  TIMESTAMP NOT NULL,
  end_time    TIMESTAMP NOT NULL,
  status      VARCHAR(20) NOT NULL,
  created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  version     INTEGER DEFAULT 0,
  CONSTRAINT fk_appointments_pets FOREIGN KEY (pet_id) REFERENCES pets (id),
  CONSTRAINT fk_appointments_owners FOREIGN KEY (owner_id) REFERENCES owners (id),
  CONSTRAINT chk_appointment_time_order CHECK (end_time > start_time)
);

-- Create indexes for performance
CREATE INDEX idx_appointments_pet_id ON appointments (pet_id);
CREATE INDEX idx_appointments_start_time ON appointments (start_time);
CREATE INDEX idx_appointments_pet_time_range ON appointments (pet_id, start_time, end_time);
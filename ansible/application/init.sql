CREATE DATABASE IF NOT EXISTS petclinic;
-- GRANT ALL PRIVILEGES ON petclinic TO 'petclinic'@'%' IDENTIFIED BY 'petclinic';
GRANT ALL PRIVILEGES ON * . * TO 'petclinic'@'petclinic';
FLUSH PRIVILEGES;
ALTER DATABASE petclinic
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

CREATE DATABASE IF NOT EXISTS springpetclinicdb;

ALTER DATABASE springpetclinicdb
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON springpetclinicdb.* TO 'springpetclinicdb'@'%' IDENTIFIED BY 'springpetclinicdb';

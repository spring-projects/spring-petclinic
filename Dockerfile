version: '3.8'

services:
  petapp:
    build: .
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/petclinic
      SPRING_DATASOURCE_USERNAME: petclinic
      SPRING_DATASOURCE_PASSWORD: petclinic

  mysql:
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_USER: petclinic
      MYSQL_PASSWORD: petclinic
      MYSQL_DATABASE: petclinic

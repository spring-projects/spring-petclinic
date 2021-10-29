#!/bin/sh
./mvnw package
docker build -t localhost:5000/petclinic .
docker-compose up -d

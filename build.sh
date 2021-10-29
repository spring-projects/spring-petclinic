#!/bin/sh
docker build -t localhost:5000/petclinic .
docker-compose up

#!/bin/bash

echo " *************************************** "
echo " ********** Build Docker Image********** "
echo " *************************************** "

sudo docker-compose -f docker-compose-buildimage.yml  build --no-cache

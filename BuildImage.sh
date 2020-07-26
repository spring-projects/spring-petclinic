#!/bin/sh

echo " *************************************** "
echo " ********** Build Docker Image********** "
echo " *************************************** "

docker-compose -f docker-compose-buildimage.yml  build --no-cache

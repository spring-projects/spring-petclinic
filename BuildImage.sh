#!/bin/sh

# copying  runtime to docker build path
sudo cp -f /home/jenkins/centos_data/apps/workspace/SpringPetClinic-Piple/target/spring-petclinic-2.1.0.BUILD-SNAPSHOT.jar /home/jenkins/centos_data/Build/

echo " *************************************** "
echo " ********** Build Docker Image********** "
echo " *************************************** "

cd /home/jenkins/centos_data/Build && docker-compose -f docker-compose-buildimage.yml  build --no-cache

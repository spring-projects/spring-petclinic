#!/bin/bash
sudo rm -rf /home/jenkins/centos_data/java-app/
git clone https://github.com/ametgud4u/spring-petclinic.git /home/jenkins/centos_data/java-app/

echo "*****************************************"
echo "*****************Build*******************"
docker run --rm -ti -v $PWD/java-app:/apps -v /root/.m2/:/root/.m2/ -w /apps/ maven3:alphine "$@"

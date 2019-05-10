#!/bin/bash

wget http://192.168.0.20:8081/artifactory/libs-snapshot-local/org/springframework/samples/spring-petclinic/maven-metadata.xml
VERSION=$(cat maven-metadata.xml | grep -m 1 -oP '(?<=<version>).*(?=</version>)')
rm -f maven-metadata.xml
wget http://192.168.0.20:8081/artifactory/libs-snapshot-local/org/springframework/samples/spring-petclinic/$VERSION/maven-metadata.xml
 
AID=$(cat maven-metadata.xml | grep -m 1 -oP '(?<=<artifactId>).*(?=</artifactId>)')
VALUE=$(cat maven-metadata.xml | grep -m 1 -oP '(?<=<value>).*(?=</value>)')
echo $AID-$VALUE.war > version
#echo "curl \"http://192.168.0.56:8081/repository/maven-snapshots/org/springframework/samples/spring-petclinic/$VERSION/" > command

echo "curl \"http://192.168.0.20:8081/artifactory/libs-snapshot-local/org/springframework/samples/spring-petclinic/$VERSION/" > command

paste -d '' command version > firsthalf
echo "\" -o /usr/share/tomcat/webapps/petclinic.war" > secondhalf
paste -d '' firsthalf secondhalf > fullcmd
sudo chmod 755 fullcmd
sudo ./fullcmd
sudo service tomcat restart
#rm -f command firsthalf fullcmd maven-metadata.xml secondhalf version
rm -f command firsthalf maven-metadata.xml secondhalf version

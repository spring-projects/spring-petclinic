#!/usr/bin/env bash

# update all existing packages
sudo yum -y update

# install wget and dos2unix
sudo yum -y install wget
sudo yum -y install dos2unix

# download java
sudo wget --no-cookies \
--no-check-certificate \
--header "Cookie: oraclelicense=accept-securebackup-cookie" \
"http://download.oracle.com/otn-pub/java/jdk/8u121-b13/e9e7ea248e2c4826b92b3f075a80e441/jdk-8u121-linux-x64.rpm" \
-O /tmp/jdk-8-linux-x64.rpm

# install java
sudo yum -y install /tmp/jdk-8-linux-x64.rpm
sudo rm /tmp/jdk-8-linux-x64.rpm

# run the application
#java -jar /tmp/spring-petclinic-1.4.2.jar &
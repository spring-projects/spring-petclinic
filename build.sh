#!/bin/bash

if [[ -e spring-petclinic ]]; then
	cd spring-petclinic
	git checkout jenkins
	git pull jenkins
else
	git clone https://github.com/hllvc/spring-petclinic.git
	cd spring-petclinic
	git checkout jenkins
fi

./mvnw package -DskipTests

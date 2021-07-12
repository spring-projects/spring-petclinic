#!/bin/bash

if [[ -f spring-petclinic ]]; then
	cd spring-petclinic
	git chekcout jenkins
	git pull jenkins
else
	git clone https://github.com/hllvc/spring-petclinic.git
	cd spring-petclinic
	git checkout jenkins
fi

./mvnw package

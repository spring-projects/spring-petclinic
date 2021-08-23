pipeline {
agent {docker 'maven:3.5-alpine' }
stages {
	stage ('Checkout') {
		steps {
			git 'https://github.com/ipop67799/spring-petclinic.git'
		}
	}
	stage ('Build') {
		steps {
			sh 'mvn clean package'
			junit '**/target/surefire-reports/TEST-*.xml'
		}
	}
}
}

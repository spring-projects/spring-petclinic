pipeline {
  
  agent any

  options {
    // Keep only 1 artifact, and many builds
    buildDiscarder(logRotator(numToKeepStr: '1000', artifactNumToKeepStr: '0'))
  }

  stages {

  	stage('Build the code') {
  		agent {
            docker { 
            	image 'openjdk:8-jdk-alpine'
            	args '-v /var/jenkins_home/.m2:/root/.m2'
            }
        }
		steps {
			sh './mvnw clean package -DskipTests'
		}
	}

	stage('Create image') {
		steps {
			sh 'pwd'
			sh 'docker build --pull -t loxon/petclinic:2.0.0 .'
			sh 'docker push loxon/petclinic:2.0.0'
		}
	}

  }

}
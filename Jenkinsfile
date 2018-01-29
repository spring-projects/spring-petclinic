pipeline {
  
  agent any

  options {
    // Keep only 1 artifact, and many builds
    buildDiscarder(logRotator(numToKeepStr: '1000', artifactNumToKeepStr: '0'))
  }

  stages {

  	stage('Build the code') {
		steps {
			script {
		  		docker.image('openjdk:8-jdk-alpine').inside('-v /var/jenkins_home/.m2:/root/.m2') {
		  			sh '''
		  				./mvnw clean package -DskipTests -T 2
		  			'''
		  		}
		  	}
			
		}
	}

	stage('Build the image') {
		steps {
			script {
		  		docker.build("loxon/petclinic:2.0.0").push()
		  	}
		}
	}

  }

}
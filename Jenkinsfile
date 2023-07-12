#!groovy

pipeline {
	agent none  stages {
  	stage('Maven Install') {
    	agent any
		
      steps {
      	sh 'mvn clean install'
      }
    }
    stage('Docker Build') {
    	agent any
      steps {
      	sh 'docker build -t registry.hub.docker.com/hybrid2k3/petclinic:1 .'
      }
    }
    stage('Docker Push') {
    	agent any
      steps {
      	withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
        	sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
          sh 'registry.hub.docker.com/hybrid2k3/petclinic:1'
        }
      }
    }
  }
}

pipeline {
	agent any  
        stages {
  	
          stage('Maven Install') {
            steps {
      	      sh 'mvn clean install'
      }
    }
    stage('Docker Build') {
      steps {
      	sh 'docker build -t shanem/spring-petclinic:latest .'
      }
    }
    stage('Docker Push') {
      steps {
      	withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
        	sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
          sh 'docker push shanem/spring-petclinic:latest'
        }
      }
    }
  }
}
